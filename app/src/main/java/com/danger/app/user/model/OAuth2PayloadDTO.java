package com.danger.app.user.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.cp4j.core.JsonUtils;

@Data
public class OAuth2PayloadDTO {
    private String id;
    private String name = "";
    private int gender = 0;
    private String avatar = "";

    public static OAuth2PayloadDTO fromFacebookAndroid(String json){
        JSONObject jo = JSON.parseObject(json);
        OAuth2PayloadDTO payload = new OAuth2PayloadDTO(); //JsonUtils.getBean(form.getPayload(), OAuth2PayloadDTO.class);
        //	"payload":"{\"id\":\"165120044600108\",\"name\":\"Joseph Wu\",\"picture\":{\"data\":{\"height\":50,\"is_silhouette\":false,\"url\":\"https:\\/\\/platform-lookaside.fbsbx.com\\/platform\\/profilepic\\/?asid=165120044600108&height=50&width=50&ext=1575774661&hash=AeTdxW2h026FsSWM\",\"width\":50}},\"first_name\":\"Joseph\",\"last_name\":\"Wu\"}",

        payload.setId(JsonUtils.getString(jo, "id"));
        payload.setName(JsonUtils.getString(jo, "name"));
        JSONObject joPicture = JsonUtils.getJSONObject(jo, "picture");
        JSONObject joPictureData = JsonUtils.getJSONObject(joPicture, "data");
        payload.setAvatar(JsonUtils.getString(joPictureData, "url"));
        return payload;
    }

    public static OAuth2PayloadDTO fromFacebookIos(String json){
        JSONObject jo = JSON.parseObject(json);
        OAuth2PayloadDTO payload = new OAuth2PayloadDTO(); //JsonUtils.getBean(form.getPayload(), OAuth2PayloadDTO.class);
        //	"payload":"{\"id\":\"165120044600108\",\"name\":\"Joseph Wu\",\"picture\":{\"data\":{\"height\":50,\"is_silhouette\":false,\"url\":\"https:\\/\\/platform-lookaside.fbsbx.com\\/platform\\/profilepic\\/?asid=165120044600108&height=50&width=50&ext=1575774661&hash=AeTdxW2h026FsSWM\",\"width\":50}},\"first_name\":\"Joseph\",\"last_name\":\"Wu\"}",

        payload.setId(JsonUtils.getString(jo, "id"));
        payload.setName(JsonUtils.getString(jo, "name"));
        JSONObject joPicture = JsonUtils.getJSONObject(jo, "picture");
        JSONObject joPictureData = JsonUtils.getJSONObject(joPicture, "data");
        payload.setAvatar(JsonUtils.getString(joPictureData, "url"));
        return payload;
    }

}
