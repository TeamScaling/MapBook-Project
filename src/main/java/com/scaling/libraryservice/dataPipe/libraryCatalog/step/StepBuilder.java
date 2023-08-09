package com.scaling.libraryservice.dataPipe.libraryCatalog.step;

import java.util.ArrayList;
import java.util.List;

public class StepBuilder {
    private List<ExecutionStep> executionSteps;

    public StepBuilder start(ExecutionStep step){
        if(executionSteps == null){
            executionSteps = new ArrayList<>();
            executionSteps.add(step);
        }
        return this;
    }

    public StepBuilder next(ExecutionStep nextStep){
        if(executionSteps == null){
            throw new IllegalArgumentException();
        }

        executionSteps.add(nextStep);
        return this;
    }

    public List<ExecutionStep> end(){
        return this.executionSteps;
    }

}
