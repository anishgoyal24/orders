package com.app.orders.entity;

import com.app.orders.utils.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "item_mst")
public class ItemDetails {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @JsonView(View.OrderDetailView.class)
    private int itemId;
    @Column(name = "item_name", nullable = false)
    @JsonView(View.OrderDetailView.class)
    private String itemName;
    private char status;
    @Column(length = 10000)
    private String description;
    @OneToMany(mappedBy = "itemDetails", cascade = CascadeType.PERSIST)
//    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<ItemPackingDetails> itemPackingDetails;
    @Column(name = "customer_allowed")
    private String customerAllowed;
    @ManyToMany(mappedBy = "itemDetails")
    private Set<Category> categories;
    String image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDetails that = (ItemDetails) o;
        return itemId == that.itemId &&
                status == that.status &&
                Objects.equals(itemName, that.itemName);
    }


    @Override
    public String toString() {
        return "ItemDetails{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", itemPackingDetails=" + itemPackingDetails +
                ", customerAllowed='" + customerAllowed + '\'' +
                ", categories=" + categories +
                ", image='" + image + '\'' +
                '}';
    }
}
