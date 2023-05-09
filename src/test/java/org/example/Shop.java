package org.example;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Shop {

    private final int shopId;
    private final int size;
    private final int delay;


    @Builder
    public Shop(int shopId, int size, int delay) {
        this.shopId = shopId;
        this.size = size;
        this.delay = delay;
    }

    public boolean hasSize() {
        return size > 0;
    }
}
