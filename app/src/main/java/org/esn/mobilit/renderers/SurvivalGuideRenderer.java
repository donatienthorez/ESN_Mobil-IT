package org.esn.mobilit.renderers;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Category;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.services.feeds.FeedService;

public class SurvivalGuideRenderer {

    private static SurvivalGuideRenderer instance;
    private final static String TAG = "SurvivalGuideRenderer";

    private SurvivalGuideRenderer(){
        instance = new SurvivalGuideRenderer();
    }

    public static SurvivalGuideRenderer getInstance(){
        if (instance == null){
            instance = new SurvivalGuideRenderer();
        }
        return instance;
    }

    public String renderSurvivalGuide(){
        String survivalContent = "", title, content;

        for (Category category : FeedService.getInstance().getSurvivalguide().getCategories()){
            title = "<h" + (category.getLevel()+1) + "><font color='"
                    + getColorByCategoryLevel(category.getLevel()) + "'>"
                    + category.getName() + "</font><h" + (category.getLevel()+1)
                    + "><br/>";

            content = "<p><font color='" +getColorByCategoryLevel(3)+ "'>" + category.getContent() + "</font></<br/>";

            survivalContent += title + content;
        }

        return survivalContent;
    }

    public String getColorByCategoryLevel(int level){
        switch (level){
            case 0 : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esngreen)); //VERT ESN
            case 1 : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esnpink)); //ROSE ESN
            case 2 : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esnorange)); //ORANGE ESN
            default : return String.valueOf(MobilITApplication.getContext().getResources().getColor(R.color.esngrey));//BLEU ESN
        }
    }

}
