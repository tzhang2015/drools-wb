package org.drools.workbench.screens.testscenario.client.reporting;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.testscenario.client.service.TestRuntimeReportingService;
import org.guvnor.common.services.shared.test.Failure;
import org.guvnor.common.services.shared.test.TestResultMessage;
import org.uberfire.client.annotations.DefaultPosition;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.workbench.model.CompassPosition;
import org.uberfire.workbench.model.Position;

@ApplicationScoped
@WorkbenchScreen(identifier = "org.kie.guvnor.TestResults")
public class TestRunnerReportingScreen
        implements TestRunnerReportingView.Presenter {

    private final TestRunnerReportingView view;

    @Inject
    public TestRunnerReportingScreen(TestRunnerReportingView view,
                                     TestRuntimeReportingService testRuntimeReportingService) {
        this.view = view;
        view.setPresenter(this);
        view.bindDataGridToService(testRuntimeReportingService);
    }

    @DefaultPosition
    public Position getDefaultPosition() {
        return CompassPosition.SOUTH;
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Reporting";
    }

    @WorkbenchPartView
    public Widget asWidget() {
        return view.asWidget();
    }

    public void onSuccess(@Observes TestResultMessage testResultMessage) {
        if (testResultMessage.wasSuccessful()) {
            view.showSuccess();
            view.setExplanation("");
        }

        view.setRunStatus(testResultMessage.getRunCount(), testResultMessage.getRunTime());

    }

    @Override
    public void onMessageSelected(Failure failure) {
        view.setExplanation(failure.getMessage());
    }

    @Override
    public void onAddingFailure(Failure failure) {
        view.showFailure();
    }
}
