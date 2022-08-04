package com.ysp.controller;

import com.ysp.bean.Blog;
import com.ysp.bean.Type;
import com.ysp.service.BlogService;
import com.ysp.service.TagService;
import com.ysp.service.TypeService;
import com.ysp.util.DateUtils;
import com.ysp.util.IPUtils;
import com.ysp.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        List<Type> types = typeService.listTypeTop(6);
        for (Type type : types) {
            for (int j = 0; j < type.getBlogs().size(); j++) {
                if (!type.getBlogs().get(j).isPublished()) {
                    type.getBlogs().remove(j);
                }
            }
        }
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types", types);
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }


    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model, HttpServletRequest request) {
        int rid = id.intValue();
        String ip  = IPUtils.getIpAddress(request);
        String value = ip + ":" + DateUtils.getCurrentTime();
        redisUtils.sSet("requestData:"+rid, value);
        int requestDataCount = new Long(redisUtils.sGetSetSize("requestData:"+rid)).intValue();
        if (null == redisUtils.get("nowIpCount:"+rid)){
            redisUtils.set("nowIpCount:"+rid,0);
        }
        String o = (String) redisUtils.get("nowIpCount:"+rid);

        int count = requestDataCount + Integer.parseInt(o);
        model.addAttribute("count",count);
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
