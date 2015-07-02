package com.application.caching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.application.caching.cache.CacheService;

@Controller
public class CacheController {

  private CacheService cacheService;

  @Autowired(required = true)
  @Qualifier(value = "cacheService")
  public void setCacheService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  @RequestMapping(value = "/cache-service", method = RequestMethod.GET)
  public String getCacheCount(Model model) {
    model.addAttribute("cacheCount", this.cacheService.getCount());
    return "cache";
  }

  @RequestMapping("/clear/{targetName}")
  public String clearCacheByTargetName(@PathVariable("targetName") String targetName) {
    this.cacheService.removeKeysStartsWith(targetName);
    return "cache";
  }

  @RequestMapping("/clear")
  public String clearAll() {
    this.cacheService.removeAll();
    return "cache";
  }

}
