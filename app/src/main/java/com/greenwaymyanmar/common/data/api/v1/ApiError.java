package com.greenwaymyanmar.common.data.api.v1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiError {

    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public static class Error implements Parcelable {
        public static final Parcelable.Creator<Error> CREATOR = new Parcelable.Creator<Error>() {
            @Override
            public Error createFromParcel(Parcel source) {
                return new Error(source);
            }

            @Override
            public Error[] newArray(int size) {
                return new Error[size];
            }
        };
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status_code")
        @Expose
        private Integer statusCode;

        public Error() {
        }

        protected Error(Parcel in) {
            this.message = in.readString();
            this.statusCode = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.message);
            dest.writeValue(this.statusCode);
        }
    }
}
