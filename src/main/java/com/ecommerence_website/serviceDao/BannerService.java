package com.ecommerence_website.serviceDao;

import java.util.List;

import com.ecommerence_website.entity.Banner;

public interface BannerService {

	
	public List<Banner> getAllBanners();
	
	public Banner create_banner(Banner banner);
	
	public void delete_banner(Integer banner_id);
}
