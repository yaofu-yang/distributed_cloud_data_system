/*
 * GianTigle Supermarkets
 * This is a simple supermarket example interface for CS6650 at Northeastern University 
 *
 * OpenAPI spec version: 1.11
 * Contact: i.gorton@northeastern.edu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.ResponseMsg;
import io.swagger.client.model.TopItems;
import io.swagger.client.model.TopStores;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsApi {
    private ApiClient apiClient;

    public ItemsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ItemsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for topItems
     * @param storeID ID of the store for top 10 sales (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call topItemsCall(Integer storeID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/items/store/{storeID}"
            .replaceAll("\\{" + "storeID" + "\\}", apiClient.escapeString(storeID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call topItemsValidateBeforeCall(Integer storeID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'storeID' is set
        if (storeID == null) {
            throw new ApiException("Missing the required parameter 'storeID' when calling topItems(Async)");
        }
        
        com.squareup.okhttp.Call call = topItemsCall(storeID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * get top 10 items sold at store
     * 
     * @param storeID ID of the store for top 10 sales (required)
     * @return TopItems
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TopItems topItems(Integer storeID) throws ApiException {
        ApiResponse<TopItems> resp = topItemsWithHttpInfo(storeID);
        return resp.getData();
    }

    /**
     * get top 10 items sold at store
     * 
     * @param storeID ID of the store for top 10 sales (required)
     * @return ApiResponse&lt;TopItems&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TopItems> topItemsWithHttpInfo(Integer storeID) throws ApiException {
        com.squareup.okhttp.Call call = topItemsValidateBeforeCall(storeID, null, null);
        Type localVarReturnType = new TypeToken<TopItems>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * get top 10 items sold at store (asynchronously)
     * 
     * @param storeID ID of the store for top 10 sales (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call topItemsAsync(Integer storeID, final ApiCallback<TopItems> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = topItemsValidateBeforeCall(storeID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TopItems>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for topStores
     * @param itemID ID of the items that we want to find top stores (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call topStoresCall(Integer itemID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/items/top10/{itemID}"
            .replaceAll("\\{" + "itemID" + "\\}", apiClient.escapeString(itemID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call topStoresValidateBeforeCall(Integer itemID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'itemID' is set
        if (itemID == null) {
            throw new ApiException("Missing the required parameter 'itemID' when calling topStores(Async)");
        }
        
        com.squareup.okhttp.Call call = topStoresCall(itemID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * get top 5 stores for item sales
     * 
     * @param itemID ID of the items that we want to find top stores (required)
     * @return TopStores
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TopStores topStores(Integer itemID) throws ApiException {
        ApiResponse<TopStores> resp = topStoresWithHttpInfo(itemID);
        return resp.getData();
    }

    /**
     * get top 5 stores for item sales
     * 
     * @param itemID ID of the items that we want to find top stores (required)
     * @return ApiResponse&lt;TopStores&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TopStores> topStoresWithHttpInfo(Integer itemID) throws ApiException {
        com.squareup.okhttp.Call call = topStoresValidateBeforeCall(itemID, null, null);
        Type localVarReturnType = new TypeToken<TopStores>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * get top 5 stores for item sales (asynchronously)
     * 
     * @param itemID ID of the items that we want to find top stores (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call topStoresAsync(Integer itemID, final ApiCallback<TopStores> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = topStoresValidateBeforeCall(itemID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TopStores>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
