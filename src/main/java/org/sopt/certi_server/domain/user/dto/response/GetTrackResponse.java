package org.sopt.certi_server.domain.user.dto.response;

import org.sopt.certi_server.domain.user.entity.enums.TrackType;

public record GetTrackResponse(TrackType track) {
    public static GetTrackResponse of(TrackType track){
        return new GetTrackResponse(track);
    }
}
