package com.wb.case1.domain.feed.model.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 词卡信息
 * @author yanghuan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {

    /**
     * 词卡id
     */
    private String cardId;

    /**
     * 词卡名称
     */
    private String cardName;
}
