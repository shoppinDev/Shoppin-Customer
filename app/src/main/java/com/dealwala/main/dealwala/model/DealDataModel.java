package com.dealwala.main.dealwala.model;

/**
 * Created by divine on 02/05/16.
 */
public class DealDataModel {

    String dealId,merchantId,shopId,dealCategory,dealSubCategory,dealTitle,dealDesc,dealAmount,dealStartDate,dealEndDate,allDays,originalValue,discountValue,discountType;
    String location,dealUsage,isActive,addedDate,categoryName,subCategoryName,merchantName,shopName;
    String saveId,customerId,shopAddress,shopLatitude,shopLongitude,amount;
    String orderId,orderDate,orderStatus;

    public DealDataModel(String dealId,String merchantId,String shopId,String dealCategory,String dealSubCategory,String dealTitle,
                         String dealDesc,String dealAmount, String dealStartDate,String dealEndDate,String allDays,String originalValue,String discountValue,String discountType,
                         String location,String dealUsage,String isActive,String addedDate,String categoryName,String subCategoryName,
                         String merchantName,String shopName){

        this.dealId = dealId;
        this.merchantId = merchantId;
        this.shopId = shopId;
        this.dealCategory = dealCategory;
        this.dealSubCategory = dealSubCategory;
        this.dealTitle = dealTitle;
        this.dealDesc = dealDesc;
        this.dealAmount = dealAmount;
        this.dealStartDate = dealStartDate;
        this.dealEndDate = dealEndDate;
        this.allDays = allDays;
        this.originalValue = originalValue;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.location = location;
        this.dealUsage = dealUsage;
        this.isActive = isActive;
        this.addedDate = addedDate;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.merchantName = merchantName;
        this.shopName = shopName;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setShopLatitude(String shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public void setShopLongitude(String shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopLatitude() {
        return shopLatitude;
    }

    public String getShopLongitude() {
        return shopLongitude;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }

    public String getSaveId() {
        return saveId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setDealAmount(String dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getDealAmount() {
        return dealAmount;
    }

    public void setDealCategory(String dealCategory) {
        this.dealCategory = dealCategory;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public void setDealSubCategory(String dealSubCategory) {
        this.dealSubCategory = dealSubCategory;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setAllDays(String allDays) {
        this.allDays = allDays;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    public void setDealEndDate(String dealEndDate) {
        this.dealEndDate = dealEndDate;
    }

    public void setDealStartDate(String dealStartDate) {
        this.dealStartDate = dealStartDate;
    }

    public void setDealUsage(String dealUsage) {
        this.dealUsage = dealUsage;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDealCategory() {
        return dealCategory;
    }

    public String getDealId() {
        return dealId;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public String getDealSubCategory() {
        return dealSubCategory;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public String getDealStartDate() {
        return dealStartDate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getDealEndDate() {
        return dealEndDate;
    }

    public String getShopId() {
        return shopId;
    }

    public String getAllDays() {
        return allDays;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getDealUsage() {
        return dealUsage;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getLocation() {
        return location;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public String getShopName() {
        return shopName;
    }
}
