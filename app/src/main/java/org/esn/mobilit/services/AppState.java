package org.esn.mobilit.services;

import android.text.TextUtils;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppState {

	@Inject
	CacheService cacheService;
	@Inject
	PreferencesService preferencesService;

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

	/**
	 * The regId of the user
	 */
	private String regId;

	private ArrayList<RSSItem> news;
	private ArrayList<RSSItem> events;
	private ArrayList<RSSItem> partners;
	private Guide guide;

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

	@SuppressWarnings("unchecked")
	public List<Country> getCountryList() {
		if (countryList == null) {
			countryList = (List<Country>) cacheService.get(ApplicationConstants.CACHE_COUNTRIES);
			return countryList == null ? new ArrayList<Country>() : countryList;
		}
		return countryList;
	}

	public String getRegId() {
		if (regId == null) {
			regId = preferencesService.getDefaults(ApplicationConstants.PREFERENCES_REG_ID);
		}
		return regId;
	}

	public ArrayList<RSSItem> getNews() {
		if (news == null || news.isEmpty()) {
			news = cacheService.getFeed(FeedType.NEWS);
		}
		return news;
	}

	public ArrayList<RSSItem> getEvents() {
		if (events == null || events.isEmpty()) {
			return cacheService.getFeed(FeedType.EVENTS);
		}
		return events;
	}

	public ArrayList<RSSItem> getPartners() {
		if (partners == null || partners.isEmpty()) {
			return cacheService.getFeed(FeedType.PARTNERS);
		}
		return partners;
	}

	public ArrayList<RSSItem> getFeed(FeedType feedType) {
		if (feedType.getFeedTypeString().equals(FeedType.EVENTS.getFeedTypeString())) {
			return getEvents();
		}

		if (feedType.getFeedTypeString().equals(FeedType.NEWS.getFeedTypeString())) {
			return getNews();
		}

		if (feedType.getFeedTypeString().equals(FeedType.PARTNERS.getFeedTypeString())) {
			return getPartners();
		}
		// Should never go there
		//FIXME improve this error
		throw new RuntimeException("getFeed : feedType unknown {} feedType");
	}

	public void setFeed(FeedType feedType, ArrayList<RSSItem> feed) {
		if (feedType.getFeedTypeString().equals(FeedType.EVENTS.getFeedTypeString())) {
			setEvents(feed);
			return;
		}

		if (feedType.getFeedTypeString().equals(FeedType.NEWS.getFeedTypeString())) {
			setNews(feed);
			return;
		}

		if (feedType.getFeedTypeString().equals(FeedType.PARTNERS.getFeedTypeString())) {
			setPartners(feed);
			return;
		}
		// Should never go there
		//FIXME improve this error
		throw new RuntimeException("setFeed : feedType unknown {} feedType");
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
		cacheService.save(ApplicationConstants.CACHE_GUIDE, guide);
	}

	public Guide getGuide() {
		if (guide != null) {
			guide = (Guide) cacheService.get(ApplicationConstants.CACHE_GUIDE);
		}
		return guide;
	}

	public void setNews(ArrayList<RSSItem> news) {
		this.news = news;
		cacheService.save(ApplicationConstants.CACHE_NEWS, section);
	}

	public void setEvents(ArrayList<RSSItem> events) {
		this.events = events;
		cacheService.save(ApplicationConstants.CACHE_EVENTS, section);
	}

	public void setPartners(ArrayList<RSSItem> partners) {
		this.partners = partners;
		cacheService.save(ApplicationConstants.CACHE_PARTNERS, section);
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

	public void setRegId(String regId) {
		this.regId = regId;
		preferencesService.setDefaults(ApplicationConstants.PREFERENCES_REG_ID, regId);
	}

	public boolean hasValidSection() {
		return getSection() != null && !TextUtils.isEmpty(getSection().getWebsite());
	}

	public String getSectionWebsite() {
		return getSection().getWebsite();
	}


}
