package org.esn.mobilit.checker;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.CountryProvider;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.List;

class CheckerCallback implements NetworkCallback<RSS> {

    Country country;
    Section section;
    String request;

    CheckerCallback(Country country, Section section, String request){
        this.country = country;
        this.section = section;
        this.request = request;
    }

    @Override
    public void onSuccess(RSS result) {

    }

    public void onNoAvailableData() {
    }

    public void onFailure(String error) {
        if (!error.equals("403 Forbidden")) {
            System.out.println(
                    "Error : " + error + "|" +
                            this.country.getName() + "|" +
                            this.section.getName() + "|" +
                            this.request
            );
        }
    }
}

public class SectionAvailableChecker {
    public static void main (String[] args){
        CountryProvider.makeCountriesRequest(new NetworkCallback<List<Country>>() {
            @Override
            public void onSuccess(List<Country> result) {
                for (Country c : result) {
                    for (Section s : c.getSections()) {
                        FeedProvider.makeEventRequest(s.getWebsite(), new CheckerCallback(c, s, "events"));
                        FeedProvider.makePartnersRequest(s.getWebsite(), new CheckerCallback(c, s, "partners"));
                        FeedProvider.makeNewsRequest(s.getWebsite(), new CheckerCallback(c, s, "news"));
                    }
                }
            }

            @Override
            public void onNoAvailableData() {
            }

            @Override
            public void onFailure(String error) {
            }
        });

    }
}

