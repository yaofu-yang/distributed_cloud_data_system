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

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.client.model.TopItemsStores;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * TopItems
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-04-04T08:27:47.105Z[GMT]")
public class TopItems {
  @SerializedName("stores")
  private List<TopItemsStores> stores = null;

  public TopItems stores(List<TopItemsStores> stores) {
    this.stores = stores;
    return this;
  }

  public TopItems addStoresItem(TopItemsStores storesItem) {
    if (this.stores == null) {
      this.stores = new ArrayList<TopItemsStores>();
    }
    this.stores.add(storesItem);
    return this;
  }

   /**
   * Get stores
   * @return stores
  **/
  @Schema(description = "")
  public List<TopItemsStores> getStores() {
    return stores;
  }

  public void setStores(List<TopItemsStores> stores) {
    this.stores = stores;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopItems topItems = (TopItems) o;
    return Objects.equals(this.stores, topItems.stores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stores);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopItems {\n");
    
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
