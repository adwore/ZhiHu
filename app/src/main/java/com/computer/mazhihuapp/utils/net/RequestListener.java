package com.computer.mazhihuapp.utils.net;
/***
 * @author Sanme
 * @date 2014-5-22
 * @email huangsanm@gmail.com
 * @desc 
 * 
 */
public interface RequestListener {

	void onSuccess(Object result);
	
	void onFailure(String errorMsg);
	
}
