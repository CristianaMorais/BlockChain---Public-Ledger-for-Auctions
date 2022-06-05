package ssd;

import java.util.ArrayList;
import java.util.List;

public class Auctions {
    String seller;
    String itemForSell;
    float currPrice;
    float timeBefore;

    List<Auctions> activeBids = new ArrayList<>();

    public Auctions(String seller, String item, float currPrice, float timeB) {
        this.seller = seller;
        this.itemForSell = item;
        this.currPrice = currPrice;
        this.timeBefore = timeB;
    }
}