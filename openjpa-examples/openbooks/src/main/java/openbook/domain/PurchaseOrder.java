/*
 * Copyright 2010 Pinaki Poddar
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
*/
package openbook.domain;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A persistent entity to demonstrate Master in a Master-Details or Composite pattern for 
 * persistent domain model.
 * <br>
 * The Purchase Order - Line Items relationship typically demonstrates a Master-Details pattern.
 * In JPA 2.0, following new features are added to support this common pattern used in domain modeling,
 * and this example demonstrates them.<br>
 * <LI> Compound, Derived identity: This feature allows the Details type to compose its identity from the
 * the Master's identity.
 * <LI> Orphan Delete or Dependent relation: This feature allows to impose composite relation semantics to
 * normally associative relation semantics implied in Java. Composite relation in persistence terms also
 * translates to deletion of Details record from database when the details lose their relation to master. 
 * <br>    
 * Besides the above two key features, this persistent type also shows usage of
 * <LI>Auto-generated identity.
 * <LI>Enum type persistent attribute.
 * <LI>Date type persistent attribute.
 * <LI>One-to-One uni-directional, immutable mapping to Customer.
 * <LI>One-to-Many bi-directional, immutable mapping to LineItem.
 * 
 * @author Pinaki Poddar
 *
 */
@SuppressWarnings("serial")
@Entity
public class PurchaseOrder implements Serializable {
    /**
     * Enumerates the status of a Purchase Order.
     *
     */
    public enum Status {PENDING, DELIVERED};
    
    @Id
    @GeneratedValue
    private long id;
    
    @OneToOne(optional=false)
    private Customer customer;
    
    @OneToMany(mappedBy="order", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    private List<LineItem> items;
    
    private Status status;
    
    private int total;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp placedOn;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp deliveredOn;
    
    /**
     * A protected constructor satisfies two purposes:<br>
     * <LI>i)  Status and creation time is set consistently.  
     * <LI>ii) OpenJPA Bytecode Enhancer requires an empty constructor for the domain classes.
     * The public constructor of Purchase Order takes a Shopping Cart as argument.
     * 
     */
    protected PurchaseOrder() {
        status = Status.PENDING;
        placedOn = new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Construct a new order by transferring the content of the given {@linkplain ShoppingCart}.
     * 
     * @param cart a non-null, non-empty Shopping cart
     * @exception NullPointerException if the cart is null
     * @exception IllegalStateException if the cart is empty
     */
    public PurchaseOrder(ShoppingCart cart) {
        this();
        if (cart == null)
            throw new NullPointerException("Can not create new Purchase Order from null Shopping Cart");
        if (cart.isEmpty())
            throw new IllegalStateException("Can not create new Purchase Order from empty Shopping Cart");
        customer = cart.getCustomer();
        Map<Book, Integer> items = cart.getItems();
        for (Map.Entry<Book, Integer> entry : items.entrySet()) {
            addItem(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Gets the immutable, auto-generated persistent identity of this Purchase Order.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the customer who placed this Purchase Order.
     * 
     * @return immutable Customer.
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Gets the status of this Purchase Order.
     * 
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * Sets the status of this Purchase Order as delivered.
     * Setting an order status as delivered nullifies the association to Line Items.
     * Nullifying this association has the important side-effect of Line Item records
     * be deleted from the database because the relation is annotated as orphan delete.
     * 
     * @exception IllegalStateException if this order has already been delivered.
     */
    public void setDelivered() {
        if (this.status == Status.DELIVERED)
            throw new IllegalStateException(this + " has been delivered");
        this.status = Status.DELIVERED;
        this.deliveredOn = new Timestamp(System.currentTimeMillis());
        this.items = null;
    }
    
    /**
     * Gets the items for this Purchase Order.
     * 
     * @return the line items of this order. The line items for a delivered order is always null.
     * @see #setDelivered()
     */
    public List<LineItem> getItems() {
        return items;
    }
    
    /**
     * Adds an item to this Purchase Order.
     * The total is adjusted as a side-effect.
     */
    void addItem(Book book, int quantity) {
        if (book == null)
            throw new NullPointerException("Can not add Line Item to Purchase Order for null Book");
        if (quantity < 1)
            throw new IllegalArgumentException("Can not add Line Item to Purchase Order for negative (=" + 
                    quantity + ") number of Book " + book);
        if (items == null)
            items = new ArrayList<LineItem>();
        items.add(new LineItem(this, items.size()+1, book, quantity));
        total += (book.getPrice() * quantity);
    }
    
    /**
     * Gets the total cost of all the items in this order.
     */
    public double getTotal() {
        return total;
    }
    
    /**
     * Gets the time when this order was placed.
     */
    public Timestamp getPlacedOn() {
        return placedOn;
    }

    /**
     * Gets the time when this order was delivered.
     * 
     * @return null if the order has not been delivered yet.
     */
    public Date getDeliveredOn() {
        return deliveredOn;
    }
}
