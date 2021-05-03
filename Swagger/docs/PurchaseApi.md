# PurchaseApi

All URIs are relative to *https://virtserver.swaggerhub.com/gortonator/GianTigle/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**newPurchase**](PurchaseApi.md#newPurchase) | **POST** /purchase/{storeID}/customer/{custID}/date/{date} | A customer purchase from a store

<a name="newPurchase"></a>
# **newPurchase**
> newPurchase(body, storeID, custID, date)

A customer purchase from a store

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.PurchaseApi;


PurchaseApi apiInstance = new PurchaseApi();
Purchase body = new Purchase(); // Purchase | items purchased
Integer storeID = 56; // Integer | ID of the store the purchase takes place at
Integer custID = 56; // Integer | customer ID making purchase
String date = "date_example"; // String | date of purchase
try {
    apiInstance.newPurchase(body, storeID, custID, date);
} catch (ApiException e) {
    System.err.println("Exception when calling PurchaseApi#newPurchase");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Purchase**](Purchase.md)| items purchased |
 **storeID** | **Integer**| ID of the store the purchase takes place at |
 **custID** | **Integer**| customer ID making purchase |
 **date** | **String**| date of purchase |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

