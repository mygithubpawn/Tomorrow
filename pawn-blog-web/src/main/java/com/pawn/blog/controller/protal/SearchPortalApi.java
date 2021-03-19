package com.pawn.blog.controller.protal;

import com.pawn.blog.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***
 * description: 搜索
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/18 15:58
 */

@RestController
@RequestMapping("/portal/search")
public class SearchPortalApi {
    @GetMapping("/doSearch")
    public Result doSearch(@RequestParam("keyword") String keyword,
                           @RequestParam("page") int page,
                           @RequestParam("size") int size) {
        return null;

    }
}
