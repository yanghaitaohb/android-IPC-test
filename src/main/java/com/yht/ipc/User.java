package com.yht.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangHaiTao on 2018/9/12.
 * E-mail: yanghaitao@ren001.com
 */
public class User implements Parcelable{
    int uid;
    String name;
    boolean isMale;
    Book book;

    public User(int uid, String name, boolean isMale) {
        this.uid = uid;
        this.name = name;
        this.isMale = isMale;
    }

    protected User(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        isMale = in.readInt() == 1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(name);
        parcel.writeInt(isMale ? 1 : 0);
        parcel.writeParcelable(book, 0);
    }
}
