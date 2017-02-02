package org.esn.mobilit.services;

import android.text.TextUtils;

import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.InjectUtil;

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

	public void setSection(Section section) {
		this.section = section;
		cacheService.save(ApplicationConstants.CACHE_SECTION, section);
	}

	public boolean hasValidSection() {
		return getSection() != null && !TextUtils.isEmpty(getSection().getWebsite());
	}

	public String getSectionWebsite() {
		return getSection().getWebsite();
	}
}
