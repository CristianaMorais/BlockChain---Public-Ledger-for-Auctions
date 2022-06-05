package ssd;

import java.util.ArrayList;
import java.util.List;

public class Auction {
    String seller;
    String itemForSell;
    float currPrice;
    float timeBefore;

    List<Auction> activeBids = new ArrayList<>();

    public Auction(String seller, String item, float currPrice, float timeB) {
        this.seller = seller;
        this.itemForSell = item;
        this.currPrice = currPrice;
        this.timeBefore = timeB;
    }
}
