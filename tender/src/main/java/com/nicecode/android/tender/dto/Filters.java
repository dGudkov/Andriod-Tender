package com.nicecode.android.tender.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 21.03.16.
 */

public class Filters extends Response {

    @SerializedName("id")
    private Integer filterId;

    @SerializedName("regions")
    private List<Integer> regions;


    @SerializedName("categories_okpd_1")
    private List<Integer> categoriesOkpd1;

    @SerializedName("categories_okpd_2")
    private List<Integer> categoriesOkpd2;

    @SerializedName("start_price")
    private Double startPrice;

    @SerializedName("comment")
    private String comment;

    @SerializedName("paid")
    private Date paidDate;

    @SerializedName("assigned")
    private Boolean assigned;

    public Filters() {
        super(0, null);
        this.comment = "Is not paid";
    }

    public Filters(Filters filter) {
        this();
        this.filterId = filter.getFilterId();
        this.regions = filter.getRegions();
        this.categoriesOkpd1 = filter.getCategoriesOkpd1();
        this.categoriesOkpd2 = filter.getCategoriesOkpd2();
        this.startPrice =filter.getStartPrice();
        this.comment = filter.getComment();
        this.paidDate = null;
    }
    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public List<Integer> getRegions() {
        return regions;
    }

    public void setRegions(List<Integer> regions) {
        this.regions = regions;
    }

    public List<Integer> getCategoriesOkpd1() {
        return categoriesOkpd1;
    }

    public void setCategoriesOkpd1(List<Integer> categoriesOkpd1) {
        this.categoriesOkpd1 = categoriesOkpd1;
    }

    public List<Integer> getCategoriesOkpd2() {
        return categoriesOkpd2;
    }

    public void setCategoriesOkpd2(List<Integer> categoriesOkpd2) {
        this.categoriesOkpd2 = categoriesOkpd2;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPaid() {
        return paidDate;
    }

    public void setPaid(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean assigned) {
        this.assigned = assigned;
    }
}
