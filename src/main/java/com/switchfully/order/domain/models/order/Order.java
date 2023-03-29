package com.switchfully.order.domain.models.order;

import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private final List<ItemGroup> itemGroupList;
    private final double totalPrice;

    public Order(List<ItemGroup> itemGroupList) {
        this.itemGroupList = itemGroupList;
        this.totalPrice = calculateTotalPrice(itemGroupList);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<ItemGroup> getItemGroupList() {
        return itemGroupList;
    }

    private double calculateTotalPrice(List<ItemGroup> itemGroupList) {
        return itemGroupList.stream().mapToDouble(ItemGroup::getPrice).sum();
    }
}
