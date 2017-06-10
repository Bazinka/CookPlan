package com.cookplan.models.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DariaEfimova on 10.06.17.
 */

public class GoogleRecipe {

    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String url;

    @SerializedName("formattedUrl")
    private String formattedUrl;

    @SerializedName("snippet")
    private String desc;

    @SerializedName("pagemap")
    private PageMap pagemap;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getFormattedUrl() {
        return formattedUrl;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        if (pagemap != null && pagemap.getImage() != null) {
            return pagemap.getImage().getSrc();
        }
        return null;
    }

    private static class PageMap {

        @SerializedName("cse_image")
        private List<CseImage> image;

        protected CseImage getImage() {
            if (image != null && image.size() == 1) {
                return image.get(0);
            }
            return null;
        }
    }


    private static class CseImage {

        @SerializedName("src")
        private String src;

        protected String getSrc() {
            return src;
        }
    }
}
