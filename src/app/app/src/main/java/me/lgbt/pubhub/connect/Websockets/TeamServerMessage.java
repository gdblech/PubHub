package me.lgbt.pubhub.connect.Websockets;

/*
 tableStatusMessage = {
    messageType: 'PlayerServerMessage',
    payload: {
        messageType: 'TableStatusRequest',
        payload: {
            QRCode: '<qr code>'
        }
    }
};
tableStatusResponse = {
    messageType: 'ServerPlayerMessage',
    payload: {
        messageType: 'TableStatusResponse',
        payload: {
            QRCode: '<qr code>',
            status: 'no team | team open | team full',
            team: {    // only if a team exists
                teamName: '<team name>',
                teamLeader: '<team leader>'
            }
        }
    }
};
createTeam = {
    messageType: 'PlayerServerMessage',
    payload: {
        messageType: 'CreateTeam',
        payload: {
            QRCode: '<qr code>',
            teamName: '<team name>'
        }
    }
};
createTeamResponse = {
    messageType: 'ServerPlayerMessage',
    payload: {
        messageType: 'CreateTeamResponse',
        payload: {
            success: true | false,
            QRCode: '<qr code>',
            teamName: '<team name>'
        }
    }
};
joinTeam = {
    messageType: 'PlayerServerMessage',
    payload: {
        messageType: 'JoinTeam',
        payload: {
            QRCode: '<qr code>'
        }
    }
};
joinTeamResponse = {
    messageType: 'ServerPlayerMessage',
    payload: {
        messageType: 'JoinTeamResponse',
        payload: {
            success: true | false,
            QRCode: '<qr code>',
            teamName: '<team name>'
        }
    }
};
 */

public class TeamServerMessage {
}
