package com.joye.jiang.common.sdk.aidl;

// Declare any non-default types here with import statements
import com.joye.jiang.common.sdk.aidl.model.Request;
import com.joye.jiang.common.sdk.aidl.model.Response;
interface IIPCService {
    Response send(in Request request);
}
