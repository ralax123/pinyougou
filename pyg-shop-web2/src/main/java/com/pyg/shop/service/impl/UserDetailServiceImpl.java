package com.pyg.shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pyg.manager.service.SellerService;
import com.pyg.pojo.TbSeller;

/**
 * 
 * @author ASUS
 *
 */
public class UserDetailServiceImpl implements UserDetailsService {

	// 注入商家service
	private SellerService sellerService;

	public SellerService getSellerService() {
		return sellerService;
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TbSeller seller = sellerService.findOne(username);
		// 判断用户是否存在
		if (seller != null && seller.getStatus().equals("1")) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			return new User(username, seller.getPassword(), authorities);
		}
		return null;
	}

}
