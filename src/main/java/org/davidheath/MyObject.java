package org.davidheath;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MyObject {
    @Id
    @GeneratedValue
    private long objId;
    private String str;

    public MyObject() {
    }
    public MyObject(String str) {
        this.str = str;
    }

    public long getObjId () {
        return objId;
    }

    public void setObjId (long objId) {
        this.objId = objId;
    }

    public String getStr () {
        return str;
    }

    public void setStr (String str) {
        this.str = str;
    }

    @Override
    public String toString () {
        return "MyObject{" +
                "objId=" + objId +
                ", str='" + str + '\'' +
                '}';
    }
}
