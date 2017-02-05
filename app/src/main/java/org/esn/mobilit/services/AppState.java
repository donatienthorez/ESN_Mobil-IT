package org.esn.mobilit.services;

import android.text.TextUtils;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppState {

	@Inject
	CacheService cacheService;

	/**
	 * The current section of the user
	 */
	private Section section;

	/**
	 * The current country of the user
	 */
	private Country country;
	/**
	 * The current country of the user
	 */
	private List<Country> countryList;

	@Inject
	public AppState() {
		InjectUtil.component().inject(this);
	}

	public Section getSection() {
		if (section == null) {
			section = (Section) cacheService.get(ApplicationConstants.CACHE_SECTION);
		}
		return section;
	}

	public Country getCountry() {
		if (country == null) {
			country = (Country) cacheService.get(ApplicationConstants.CACHE_COUNTRY);
		}
		return country;
	}

	public List<Country> getCountryList() {
		if (countryList == null) {
			countryList = (List<Country>) cacheService.get(ApplicationConstants.CACHE_COUNTRY);
		}
		return countryList;
	}

	public void setSection(Section section) {
		this.section = section;
		cacheService.save(ApplicationConstants.CACHE_SECTION, section);
	}

	public void setCountry(Country country) {
		this.country = country;
		cacheService.save(ApplicationConstants.CACHE_COUNTRY, country);
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
		cacheService.save(ApplicationConstants.CACHE_COUNTRIES, countryList);
	}

	public boolean hasValidSection() {
		return getSection() != null && !TextUtils.isEmpty(getSection().getWebsite());
	}

	public String getSectionWebsite() {
		return getSection().getWebsite();
	}
}
