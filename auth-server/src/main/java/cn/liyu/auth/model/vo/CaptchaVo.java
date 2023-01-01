package cn.liyu.auth.model.vo;

import lombok.Data;

@Data
public class CaptchaVo {
    private String img;
    private String captchaId;
}
