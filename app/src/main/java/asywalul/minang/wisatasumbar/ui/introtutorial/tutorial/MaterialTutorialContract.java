package asywalul.minang.wisatasumbar.ui.introtutorial.tutorial;


import java.util.List;

import asywalul.minang.wisatasumbar.ui.introtutorial.MaterialTutorialFragment;
import asywalul.minang.wisatasumbar.ui.introtutorial.TutorialItem;


/**
 * @author rebeccafranks
 * @since 15/11/09.
 */
public interface MaterialTutorialContract {

    interface View {
        void showNextTutorial();
        void showEndTutorial();
        void setBackgroundColor(int color);
        void showDoneButton();
        void showSkipButton();
        void setViewPagerFragments(List<MaterialTutorialFragment> materialTutorialFragments);
    }

    interface UserActionsListener {
        void loadViewPagerFragments(List<TutorialItem> tutorialItems);
        void doneOrSkipClick();
        void nextClick();
        void onPageSelected(int pageNo);
        void transformPage(android.view.View page, float position);

        int getNumberOfTutorials();
    }

}
