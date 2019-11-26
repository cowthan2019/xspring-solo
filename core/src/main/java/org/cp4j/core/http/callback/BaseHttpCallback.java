package org.cp4j.core.http.callback;


import org.cp4j.core.http.AyoRequest;

public abstract class BaseHttpCallback<T> {

	public BaseHttpCallback(){
	}
	
	public abstract void onFinish(AyoRequest request, boolean isSuccess, FailInfo failInfo, T data);

}
