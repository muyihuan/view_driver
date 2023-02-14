package com.github.case1;

import com.github.case1.domain.comment.CommentDomainService;
import com.github.case1.domain.comment.model.CommentModel;
import com.github.case1.domain.feed.FeedDomainService;
import com.github.case1.domain.feed.FeedLogoService;
import com.github.case1.domain.feed.model.FeedModel;
import com.github.case1.domain.feed.model.property.BackgroundInfo;
import com.github.case1.domain.feed.model.ugc.AudioContentInfo;
import com.github.case1.domain.feed.model.ugc.ImageContentInfo;
import com.github.case1.domain.feed.model.ugc.VideoContentInfo;
import com.github.case1.domain.live.LiveDomainService;
import com.github.case1.domain.live.model.LiveRoomModel;
import com.github.case1.domain.member.MemberDomainService;
import com.github.case1.domain.member.model.MemberModel;
import com.github.case1.domain.state.StateDomainService;
import com.github.case1.domain.state.model.StateModel;
import com.github.case1.domain.tag.TagDomainService;
import com.github.case1.domain.tag.model.TagModel;
import com.github.case1.domain.user.UserDomainService;
import com.github.case1.domain.user.UserDressService;
import com.github.case1.domain.user.model.HeadDressModel;
import com.github.case1.domain.user.model.UserModel;
import com.github.case1.domain.user.model.UserSocialStatusInfo;
import com.github.case1.view.*;
import com.github.case1.view.ugc.AudioView;
import com.github.case1.view.ugc.ImageView;
import com.github.case1.view.ugc.VideoView;

import java.util.*;
import java.util.stream.Collectors;

public class Case {

    private FeedDomainService feedDomainService = new FeedDomainService();

    private UserDomainService userDomainService = new UserDomainService();

    private MemberDomainService memberDomainService = new MemberDomainService();

    private UserDressService userDressService = new UserDressService();

    private TagDomainService tagDomainService = new TagDomainService();

    private CommentDomainService commentDomainService = new CommentDomainService();

    private StateDomainService stateDomainService = new StateDomainService();

    private FeedLogoService feedLogoService = new FeedLogoService();

    private LiveDomainService liveDomainService = new LiveDomainService();

    public List<FeedView> build(List<Long> feedIds) {
        // 获取feed信息
        Map<Long, FeedModel> feedModelMap = feedDomainService.batchGetFeeds(feedIds);
        List<FeedModel> order = new ArrayList<>(feedModelMap.values());
        for(FeedModel feedModel : feedModelMap.values()) {
            if(feedModel.getRepost() != null) {
                feedModelMap.put(feedModel.getFeedId(), feedModel.getRepost().getRootFeedSource());
                order.add(0, feedModel.getRepost().getRootFeedSource());
            }
        }
        List<Long> allFeedIds = order.stream().map(FeedModel::getFeedId).collect(Collectors.toList());
        // 收集用户id
        Set<String> uidList = order.stream().map(FeedModel::getUid).collect(Collectors.toSet());
        // 获取话题信息
        List<Long> tagIds = new ArrayList<>();
        order.forEach(feedModel -> {
            tagIds.addAll(feedModel.getTagIds());
        });
        Map<Long, TagModel> tagModelMap = tagDomainService.batchGetTags(tagIds);
        // 获取评论信息
        Map<Long, List<CommentModel>> commentListMap = commentDomainService.queryCommentList(allFeedIds, 1, 10);
        for(Long topicId : commentListMap.keySet()) {
            List<CommentModel> commentModels = commentListMap.get(topicId);
            List<CommentModel> copy = new ArrayList<>();
            for(CommentModel commentModel : commentModels) {
                if(commentModel.getRepliedComment() != null) {
                    copy.add(commentModel.getRepliedComment());
                }
            }
            copy.addAll(commentModels);
            commentListMap.put(topicId, copy);

            uidList.addAll(copy.stream().map(CommentModel::getUid).collect(Collectors.toList()));
        }
        // 获取评论数量
        Map<Long, Integer> commentCountMap = commentDomainService.queryCommentCount(allFeedIds);
        // 获取用户数据
        Map<String, UserModel> userModelMap = userDomainService.batchGetUsers(uidList);
        // 获取会员数据
        Map<String, MemberModel> memberModelMap = memberDomainService.batchGetMemberModel(uidList);
        // 获取用户社交状态
        Map<String, UserSocialStatusInfo> userSocialStatusInfoMap = userDomainService.batchGetUserSocialStatusInfos(uidList);
        // 获取用户装扮
        Map<String, HeadDressModel> headDressModelMap = userDressService.batchGetUsers(uidList);
        // 获取在线状态
        Map<String, StateModel> stringStateModelMap = stateDomainService.batchGetStateModel(uidList);
        // 获取feed logo信息
        Map<Long, List<String>> logoMap = feedLogoService.batchGetFeedLogos(feedIds);
        // 获取直播房间信息
        Map<String, LiveRoomModel> liveRoomModelMap = liveDomainService.batchGetLiveRoom(uidList);

        Map<Long, FeedView> feedViewMap = new HashMap<>();
        for(FeedModel feedModel : order) {
            feedViewMap.put(feedModel.getFeedId(), new FeedView());
        }

        for(FeedModel feedModel : order) {
            FeedView feedView = feedViewMap.get(feedModel.getFeedId());

            feedView.setId(feedModel.getFeedId());
            feedView.setFeedType(feedModel.getContentType().getType());
            feedView.setTitle(feedModel.getText().getContent());

            // 构建@信息
            feedView.setAtInfos(feedModel.getAtInfoList());

            // 构建图片信息
            List<ImageView> imageViewList = new ArrayList<>();
            List<ImageContentInfo> imageList = feedModel.getMultiImage().getImageList();
            for(ImageContentInfo imageContentInfo : imageList) {
                ImageView imageView = new ImageView();
                imageView.setIconImg(imageContentInfo.getIconImg());
                imageView.setIconImgLarge(imageContentInfo.getIconImgLarge());
                imageViewList.add(imageView);
            }
            feedView.setImageList(imageViewList);

            // 构建语音信息
            AudioContentInfo audio = feedModel.getAudio();
            AudioView audioView = new AudioView();
            audioView.setAudioUrl(audio.getAudioUrl());
            audioView.setAudioLength(audio.getAudioLength());
            audioView.setAudioSize(audio.getAudioSize());
            feedView.setAudio(audioView);

            // 构建视频信息
            VideoContentInfo video = feedModel.getVideo();
            VideoView videoView = new VideoView();
            videoView.setPlayUrl(video.getPlayUrl());
            videoView.setVideoLength(video.getVideoLength());
            feedView.setVideo(videoView);

            // 构建转发信息
            FeedModel rootFeedSource = feedModel.getRepost().getRootFeedSource();
            FeedView rootFeedView = feedViewMap.get(rootFeedSource.getFeedId());
            feedView.setRootFeed(rootFeedView);

            // 构建用户视图
            UserModel userModel = userModelMap.get(feedModel.getUid());
            UserInfoView userInfoView = new UserInfoView();
            userInfoView.setUid(feedModel.getUid());
            userInfoView.setUserName(userModel.getUsername());
            userInfoView.setUserIcon(userModel.getUserIcon());
            userInfoView.setGender(userModel.getGender());

            // 构建会员等级
            MemberModel memberModel = memberModelMap.get(feedModel.getUid());
            userInfoView.setMemberLevel(memberModel.getMemberLevel());

            // 构建用户社交状态
            UserSocialStatusInfo userSocialStatusInfo = userSocialStatusInfoMap.get(feedModel.getUid());
            UserSocialStatusView userSocialStatusView = new UserSocialStatusView();
            userSocialStatusView.setUid(userSocialStatusInfo.getUid());
            userSocialStatusView.setStateIcon(userSocialStatusInfo.getStateIcon());
            userInfoView.setUserSocialStatusInfo(userSocialStatusView);

            // 构建用户装扮
            HeadDressModel headDressModel = headDressModelMap.get(feedModel.getUid());
            HeadDressView headDressView = new HeadDressView();
            headDressView.setDressUrl(headDressModel.getDressUrl());
            headDressView.setDressSort(headDressModel.getDressSort());
            headDressView.setUrlType(headDressModel.getUrlType());
            headDressView.setDressType(headDressModel.getDressType());
            userInfoView.setHeadDress(headDressView);

            feedView.setAuthor(userInfoView);

            // 构建话题信息
            List<TagView> tagList = new ArrayList<>();
            List<Long> tagIdList = feedModel.getTagIds();
            for(Long tagId : tagIdList) {
                TagModel tagModel = tagModelMap.get(tagId);
                TagView tagView = new TagView();
                tagView.setId(tagModel.getId());
                tagView.setName(tagModel.getName());
                tagView.setFollowFlag(tagModel.getFollowFlag());
                tagView.setMarkerUrl(tagModel.getMarkerUrl());
                tagView.setIsMasterTag(tagModel.getIsMasterTag());
                tagList.add(tagView);
            }
            feedView.setTagList(tagList);

            // 构建评论信息
            List<CommentView> comments = new ArrayList<>();
            List<CommentModel> commentModels = commentListMap.get(feedModel.getFeedId());
            for(CommentModel commentModel : commentModels) {
                CommentView commentView = new CommentView();
                commentView.setId(commentModel.getId());
                commentView.setContent(commentModel.getContent());

                // 构建评论内容
                ImageContentInfo image = commentModel.getImage();
                ImageView commentImage = new ImageView();
                commentImage.setIconImg(image.getIconImg());
                commentImage.setIconImgLarge(image.getIconImgLarge());
                commentView.setImage(commentImage);

                // 构建被恢复评论
                CommentModel repliedComment = commentModel.getRepliedComment();
                if(repliedComment != null) {
                    CommentView repliedCommentView = comments.stream().filter(commentView1 -> commentView1.getId() == repliedComment.getId()).findFirst().get();
                    commentView.setRepliedComment(repliedCommentView);
                }
                else {
                    commentView.setRepliedComment(null);
                }

                UserModel commentUserModel = userModelMap.get(commentModel.getUid());
                UserInfoView commentUserInfoView = new UserInfoView();
                userInfoView.setUid(commentModel.getUid());
                userInfoView.setUserName(commentUserModel.getUsername());
                userInfoView.setUserIcon(commentUserModel.getUserIcon());
                userInfoView.setGender(commentUserModel.getGender());
                commentView.setAuthorInfo(commentUserInfoView);

                comments.add(commentView);
            }
            feedView.setComments(comments);

            // 构建评论数量
            Integer commentCount = commentCountMap.get(feedModel.getFeedId());
            feedView.setCommentCount(commentCount);

            // 构建feed背景
            BackgroundInfo backgroundInfo = feedModel.getExtraInfo().getBackgroundInfo();
            BackgroundView backgroundImgInfo = new BackgroundView();
            backgroundImgInfo.setBackgroundImgId(backgroundInfo.getBackgroundImgId());
            backgroundImgInfo.setStaticImgUrl(backgroundInfo.getStaticImgUrl());
            backgroundImgInfo.setDynamicImgUrl(backgroundInfo.getDynamicImgUrl());
            feedView.setBackgroundImgInfo(backgroundImgInfo);

            // 构建推荐信息
            RecommendView recommendInfo = new RecommendView();
            recommendInfo.setRecommendTitle("");
            recommendInfo.setRecommendUnifyJump("");
            recommendInfo.setRecommendNightLogo("");
            recommendInfo.setRecommendDayLogo("");
            feedView.setRecommendInfo(recommendInfo);

            // 构建用户在线状态
            StateModel stateModel = stringStateModelMap.get(feedModel.getUid());
            UserStatusDrainageView userStatusDrainageInfo = new UserStatusDrainageView();
            userStatusDrainageInfo.setJumpUrl(stateModel.getJumpUrl());
            userStatusDrainageInfo.setDesc(stateModel.getDesc());
            userStatusDrainageInfo.setType(stateModel.getType());
            userStatusDrainageInfo.setGameTypeId(stateModel.getGameTypeId());
            feedView.setUserStatusDrainageInfo(userStatusDrainageInfo);

            // 构建feed的logo
            feedView.setLogoList(logoMap.get(feedModel.getFeedId()));

            // 构建作者语音方信息
            LiveRoomModel liveRoomModel = liveRoomModelMap.get(feedModel.getUid());
            LiveRoomView liveRoomInfo = new LiveRoomView();
            liveRoomInfo.setJump(liveRoomModel.getJump());
            feedView.setLiveRoomInfo(liveRoomInfo);
        }

        List<FeedView> result = new ArrayList<>();
        for(Long feedId : feedIds) {
            result.add(feedViewMap.get(feedId));
        }

        return result;
    }
}
