package com.lee.protobuf;

import com.google.protobuf.GeneratedMessageV3;

public interface ProtobufSerializableV3<T extends GeneratedMessageV3> {

    default byte[] toByteArray(){return copyTo().toByteArray();}

    T copyTo();

    void parseFrom(byte[] bytes);

    void copyFrom(T t);

}
