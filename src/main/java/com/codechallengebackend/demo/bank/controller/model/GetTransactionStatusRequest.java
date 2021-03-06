package com.codechallengebackend.demo.bank.controller.model;

import com.codechallengebackend.demo.bank.controller.model.validation.ChannelValidated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GetTransactionStatusRequest {

    @NotNull
    @NotEmpty
    private String reference;

    @NotNull
    @NotEmpty
    @ChannelValidated
    private Channel channel;

    public GetTransactionStatusRequest() {}

    public GetTransactionStatusRequest(@NotNull @NotEmpty String reference, @NotNull @NotEmpty Channel channel) {
        this.reference = reference;
        this.channel = channel;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
