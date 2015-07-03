package com.application.caching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.application.caching.cache.service.CacheService;

/**
 * 
 * @author vsinha
 * @version 1.0
 * @date 06-July-2015
 */
@Controller
public class CacheController {

  private CacheService cacheService;

  @Autowired(required = true)
  @Qualifier(value = "ehcacheService")
  public void setCacheService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  @RequestMapping(value = "/cache", method = RequestMethod.GET)
  public String getCacheCount(Model model) {
    model.addAttribute("cacheCount", this.cacheService.getCount());
    return "cache";
  }

  @RequestMapping("/cache/{targetName}")
  public String clearCacheByTargetName(@PathVariable("targetName") String targetName) {
    this.cacheService.removeKeysStartsWith(targetName);
    return "redirect:/cache";
  }

  @RequestMapping("/clearAll")
  public String clearAll() {
    this.cacheService.removeAll();
    return "redirect:/cache";
  }

}
