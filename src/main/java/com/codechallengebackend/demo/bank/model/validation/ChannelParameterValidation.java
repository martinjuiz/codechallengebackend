package com.codechallengebackend.demo.bank.model.validation;

import com.codechallengebackend.demo.bank.model.Channel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ChannelParameterValidation implements ConstraintValidator<ChannelValidated, String> {

    @Override
    public void initialize(ChannelValidated channelValidated) {

    }

    @Override
    public boolean isValid(String channel, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Channel.values())
                .anyMatch(ch -> ch.name().equals(channel));
    }
}
