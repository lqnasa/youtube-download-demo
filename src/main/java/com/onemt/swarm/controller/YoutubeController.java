package com.onemt.swarm.controller;

import com.onemt.swarm.enums.ExceptionCodeEnums;
import com.onemt.swarm.util.HttpClientUtils;
import com.onemt.swarm.util.ResultUtils;
import com.onemt.swarm.vo.ResultVO;
import com.onemt.swarm.vo.Video;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/1/11.
 */
@RestController
public class YoutubeController {

    private final static String GET_VIDEO_INFO_URL = "https://www.youtube.com/get_video_info?video_id=%s";
    private final static String ERROR_CODE = "errorcode";

    @GetMapping("/videoId/{videoId}")
    @ResponseBody
    public ResultVO<List<Video>> getDownloadInfo(@PathVariable String videoId) {
        List<Video> videos = new ArrayList<>();
        if (StringUtils.isBlank(videoId)) {
            return ResultUtils.error(ExceptionCodeEnums.PARAM_ERROR);
        }
        String html = HttpClientUtils.doGet(String.format(GET_VIDEO_INFO_URL, videoId));
        if (html.contains(ERROR_CODE)) {
            return ResultUtils.error(ExceptionCodeEnums.PLAY_ERROR);
        }

        Pattern p = Pattern.compile("url_encoded_fmt_stream_map=([^&]*)");
        Matcher m = p.matcher(html);
        String urlStr = "";
        while (m.find()) {
            urlStr = m.group(1);
            break;
        }

        if (StringUtils.isBlank(urlStr)) {
            System.out.println("Found zero or too many stream maps.");
            return ResultUtils.error(ExceptionCodeEnums.ANALYSIS_FAIL);
        }

        try {
            urlStr = URLDecoder.decode(urlStr, "UTF-8");
            String[] typeStrs = urlStr.split(",");
            for (String typeStr : typeStrs) {
                String[] urlStrs = typeStr.split("&");
                Map<String, String> map = Arrays.stream(urlStrs).map(str -> str.split("=")).collect(Collectors.toMap(str -> str[0], str -> {
                    try {
                        String value = URLDecoder.decode(str[1], "UTF-8");
                        return URLDecoder.decode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }));

                Pattern compile = Pattern.compile("clen=(\\d+)");
                Matcher urlMatcher = compile.matcher(map.get("url"));
                while (urlMatcher.find()){
                    map.put("clen",urlMatcher.group(1));
                    break;
                }

                Video video = BeanUtils.instantiateClass(Video.class);
                BeanWrapper beanWrapper = new BeanWrapperImpl(video);
                beanWrapper.setPropertyValues(map);
                videos.add(video);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResultUtils.error(ExceptionCodeEnums.ANALYSIS_FAIL);
        }

        return ResultUtils.success(videos);
    }

}
