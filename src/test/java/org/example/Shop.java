package org.example;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Shop {

    private final int shopId;
    private final int size;

    @Builder
    public Shop(int shopId, int size) {
        this.shopId = shopId;
        this.size = size;
    }

    public boolean hasSize() {
        return size > 0;
    }
}
