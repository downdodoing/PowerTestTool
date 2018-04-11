package com.meizu.powertesttool.IPC;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    public String sname;
    public int age;
    public String sex;
    public int sno;
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student() {
    }

    private Student(Parcel in) {
        readParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sno);
        dest.writeString(sname);
        dest.writeInt(age);
        dest.writeString(sex);
    }

    public void readParcel(Parcel in) {
        sno = in.readInt();
        sname = in.readString();
        age = in.readInt();
        sex = in.readString();
    }
}
