package com.smarthire.resume.exception;

import com.smarthirepro.core.exception.BusinessRuleException ;

public class InvalidScoreWeightsException extends BusinessRuleException  {
    public InvalidScoreWeightsException(String message) {
        super(message);
    }
}
