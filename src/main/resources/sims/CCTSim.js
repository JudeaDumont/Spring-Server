function redisMessage() {
    return new Promise(function (reject) {
        let redis = require('redis');
        try {
            let logPathTopDir = "C:\\Users\\jdumont\\IdeaProjects\\acecctFin\\webroot\\logFiles\\";
            let logPathNumber = 0;
            let logPath = "log" + logPathNumber +
                ".txt";

            let fileSystemWriter = null;
            let fs = new Promise(function () {
                fileSystemWriter = require('fs');
            });
            fs.then(function () {
            }).catch(function (err) {
                console.log('CANNOT WRITE FILES' + err);
            });


            redisClient = redis.createClient();//127.0.0.1 and 6379 default

            redisSubscriberClient = redis.createClient();
            redisSubscriberClient.subscribe('APP.' + 'inChannel.put.server.log.request');
            setTimeout(function () {
                redisClient.publish("APP.outChannel.server.log.request", 'serverDirectory Request');
            }, 500);
            redisSubscriberClient.subscribe('APP.inChannel');
            redisSubscriberClient.subscribe('APP.inChannel.put.config');
            redisSubscriberClient.subscribe('APP.inChannel.put.ini');
            redisClient.publish("APP.outChannel.logPath", logPathTopDir + logPath);
            fileSystemWriter.appendFile(logPathTopDir + logPath,
                logPathTopDir + logPath + '\n'
                , (err) => {
                    if (err) {
                    }
                });

            redisClient.on('error', function (error) {
                reject(error);
            });

            redisSubscriberClient.on('error', function (error) {
                reject(error);
            });


            let configMessage;

            let obj = {};
            obj.iniFile =
                `ini file testing from redis flood
            line 2
            this better show up correctly
            with the new lines`;

            redisSubscriberClient.on('message', function (channel, message) {
                if (channel === 'APP.inChannel') {
                    if (message !== '') {
                        // console.log(message);
                    }
                    if (message === 'requestIni') {
                        console.log('requestIniConfigHIT');
                        // console.log(message);
                        redisClient.publish('APP.outChannel.ini', obj.iniFile);
                        ///USING a specific buffer: outChannel.specific
                        //Otherwise sends upblish to outchannel, where server will have to test object for type given a property
                    }
                    else if (message === 'requestSessionConfig') {
                        // console.log(message);
                        redisClient.publish("APP.outChannel.config", configMessage);
                        ///USING a specific buffer: outChannel.specific
                        //Otherwise sends upblish to outchannel, where server will have to test object for type given a property
                    }
                    else if (message === 'requestLogPath') {
                        // console.log(message);
                        redisClient.publish("APP.outChannel.logPath", logPathTopDir + logPath);
                        ///USING a specific buffer: outChannel.specific
                        //Otherwise sends upblish to outchannel, where server will have to test object for type given a property
                    }
                    else if (message === 'requestNewLog') {
                        logPath = "log" + ++logPathNumber + ".txt";
                        fileSystemWriter.appendFile(logPathTopDir + logPath, logPathTopDir + logPath + '\n',
                            (err) => {
                                if (err) {
                                }
                            });
                        // fileSystemWriter.appendFile(logPathTopDir + logPath, "",
                        //     (err) => {
                        //         if (err) {
                        //         }
                        //     });
                        redisClient.publish("APP.outChannel.logPath", logPathTopDir + logPath);
                    }
                    else if (message === 'requestDataSchema') {
                        console.log("requested Data Schema");
                        // logPath = "log" + ++logPathNumber + ".txt";
                        // fileSystemWriter.appendFile(logPathTopDir + logPath, logPathTopDir + logPath + '\n',
                        //     (err) => {
                        //         if (err) {
                        //         }
                        //     });
                        // fileSystemWriter.appendFile(logPathTopDir + logPath, "",
                        //     (err) => {
                        //         if (err) {
                        //         }
                        //     });
                        redisClient.publish("APP.requestDataSchema", JSON.stringify({
                            status: {
                                YEAR: "String",
                                TIME1: "String",
                                TIME2: "String",
                                TIME3: "String",
                                TIME4: "String",
                                AZ: "Double",
                                EL: "Double",
                                RANGE: "String",
                                RRATE: "String",
                                XMTR: "String",
                                M34: "String",
                                SAB: "Integer",
                                ANTMODE: "String",
                                CMDBUFF: "Integer",
                                ACTIVE: "String",
                                ECHO_MODE: "String",
                                AGC1: "String",
                                AGC2: "String",
                                AGC3: "String",
                                AGC4: "String",
                                RWS: "String",
                                IDLE_MODE: "String",
                                CMD_RATE: "Integer",
                                CMD_ENABLED: "String",
                                CMD_MODE: "Integer",
                                AZ_DELTA: "Double",
                                EL_DELTA: "Double",
                                RANGE_DELTA: "Integer",
                                PCMSIM_ENABLED: "String",
                                SV_CMDS_SENT: "Integer",
                                SV_CMDS_RCVD: "Integer",
                                AGC_VALUE: "Integer"
                            },
                            update: {
                                CMD_SRC: "String",
                                CMD_DST: "String",
                                CMD_ECHO: "String",
                                CMD_TERM: "String",
                                PRIOPEN: "String",
                                PRICONNECT: "String",
                                SECOPEN: "String",
                                SECCONNECT: "String",
                                SYSTIME: "String",
                                CLIENTS: "String",
                                PRI_ADCCP_MSG_SENT: "Integer",
                                PRI_ADCCP_MSG_RCVD: "Integer",
                                SEC_ADCCP_MSG_SENT: "Integer",
                                SEC_ADCCP_MSG_RCVD: "Integer",
                                PRI_CONNECT_FAILURE: "String",
                                SEC_CONNECT_FAILURE: "String",
                                TASKSTATUS: "String"
                            }
                        }));
                    }
                    else if (message === 'requestChannelSchema') {
                        console.log("requested Channel Schema");
                        // logPath = "log" + ++logPathNumber + ".txt";
                        // fileSystemWriter.appendFile(logPathTopDir + logPath, logPathTopDir + logPath + '\n',
                        //     (err) => {
                        //         if (err) {
                        //         }
                        //     });
                        // fileSystemWriter.appendFile(logPathTopDir + logPath, "",
                        //     (err) => {
                        //         if (err) {
                        //         }
                        //     });
                        redisClient.publish("APP.requestChannelSchema", JSON.stringify({
                            //If there is no string, it will map that channel name to a message of the same name
                            //Otherwise it will map to a raw message of the specified name
                            update: "",
                            status: "",
                            logPath: "",
                            config: "",
                        }));
                    }
                }

                else if (channel === 'APP.inChannel.put.config') { //a client puts the config back into the CCT
                    // console.log(message);
                }

                else if (channel === 'APP.inChannel.put.ini') { //a client puts the INI back into the CCT
                    console.log('new ini=' + message);
                    obj.iniFile = message;
                }
                else if (channel === 'APP.inChannel.put.server.log.request') { //a client puts the INI back into the CCT
                    console.log(message);
                }
            });


            //Expect to get a new string that has
            //Client count, CCT time, source and destination, ternary/UDP, XMTR state
            let i = 0;
            let j = 0;
            let k = 0;
            let l = 0;
            let m = 0;
            let logLine = 0;
            let messageNumber = 0;
            let taskindex = 0;
            let logging = false;

            setTimeout(function () {
                taskindex = 2;
            }, 5000);

            setTimeout(function () {
                redisClient.publish("APP.outChannel.config", configMessage);
                taskindex = 0;
                logging = true;
            }, 10000);
            let g = 0;

            let f = 0;

            let statusingArr = [
                '{"primary":true, "secondary":true}',
                '{"primary":false, "secondary":true}',
                '{"primary":true, "secondary":false}',
                '{"primary":false, "secondary":false}'
            ];

            let statusMessage = '';
            let updateMessage = '';

            let n = 0, o = 0, p = 0, q = 0, r = 0, s = 0;

            let isin = 0;
            let icos = 0;


            let number;
            let number2;

            function redisFlood() {
                isin += 0.01;
                icos += 0.01;

                let s2 = 'FROM REDIS FLOOD ///// FROM REDIS FLOOD FROM REDIS FLOOD ///// FROM REDIS FLOOD FROM REDIS FLOOD ///// FROM REDIS FLOOD ';
                if (logging) {
                    fileSystemWriter.appendFile(logPathTopDir + logPath,
                        (function () {
                            let aggregator = "";
                            for (let i = 0; i < 1; i++) {
                                aggregator += (++logLine + ': ' + s2 + '\n');
                            }
                            return aggregator;
                        })()

                        , (err) => {
                            if (err) {
                            }
                        });
                }

                // let statusMessage2 =
                //     '{"RECEIVE":' + i + ',' +
                //     '"TRANSMIT":' + j + ',' +
                //     '"QUEUESIZE":' + k + ',' +
                //     '"BUFFERSIZE":' + l + ',' +
                //     '"NUMSYNCCHAR":' + m + ',' +
                //     '"SYSTIME":"' + new Date().toLocaleDateString() + ' ' + new Date().toLocaleTimeString() + '",' +
                //     '"YEAR":"2017",' +
                //     '"TIME":"257/00118",' +
                //     '"TIME2":"' + ((Date.now()).toString().substring(8)) + '  ",' +
                //     '"SVCMDS":"' + Math.floor(300 * Math.random()) + '  ",' +
                //     '"CLIENTS":"' + Math.floor(2 * Math.random()) + '  ",' +
                //     '"XMTR":' + xmtrArr[i % 4] + ',' +
                //     '"TASKSTATUS":' + taskedArr[g % 4] + ',' +
                //     '"STATUSING":' + statusingArr[f % 4] + ',' +
                //     '"TIME3":"257/00118",' +
                //     '"TIME4":"142/58584",' +
                //     '"AZ":' + Math.floor(Math.random() * 150) + ',' +
                //     '"EL":' + Math.floor(Math.random() * 150) + ',' +
                //     '"AZ_DELTA":"0.016",' +
                //     '"EL_DELTA":"0.019",' +
                //     '"NET_IN":' + Math.floor(Math.random() * 150) + ',' +
                //     '"NET_OUT":' + Math.floor(Math.random() * 150) + ',' +
                //     '"AGC2":' + Math.floor(Math.random() * 150) + ',' +
                //     '"RANGE":"INVALID",' +
                //     '"RRATE":"INVALID",' +
                //     '"M34":"OFF",' +
                //     '"SAB":"35",' +
                //     '"ANTMODE":"ATK",' +
                //     '"CMDBUFF":"0",' +
                //     '"ACTIVE":"PRI",' +
                //     '"ECHO_MODE":"DISABLED",' +
                //     '"AGC1":"0x76",' +
                //     '"AGC3":"0x00",' +
                //     '"AGC4":"0x00",' +
                //     '"RWS":"On",' +
                //     '"IDLE_MODE":"STones",' +
                //     '"CMD_RATE":"1000",' +
                //     '"CMD_ENABLED":"Yes",' +
                //     '"CMD_MODE":"1",' +
                //     '"RANGE_DELTA":"-11170.786",' +
                //     '"PCMSIM_ENABLED":"FALSE"\}';

                let time = new Date().toLocaleDateString('en-US') + '' + new Date().toLocaleTimeString('en-US');
                let xmtrArr = ['"DUMMYLOAD"', '"ANTENNA"'];
                let clientArr = ['"RED-IGC3M-W-CP"', '""', '"RED-IGC3M-W-CP"', '""'];
                let antModeArr = ["ATK"];
                if (i % 64 === 0) {
                    number = Math.random() * 32 - 16;
                    number2 = Math.random() * 8 - 4;
                }
                statusMessage = `
       {
        "YEAR":"2017",
        "TIME1":"257/${((Date.now()).toString().substring(5))}",
        "TIME2":"257/${((Date.now()).toString().substring(5))}",
        "TIME3":"257/${((Date.now()).toString().substring(5))}",
        "TIME4":"142/${((Date.now()).toString().substring(5))}",
        "AZ":"${number + Math.sin(isin) * 20 + 240}",
        "EL":"${number2 + Math.cos(icos) * 20 + 65}",
        "RANGE":"INVALID",
        "RRATE":"INVALID",
        "XMTR":${xmtrArr[i % 2]},
        "M34":"${(i % 2 === 0 ? 'OFF' : 'ON')}",
        "SAB":"${Math.floor(Math.random() * 24)}",
        "ANTMODE":"ATK",
        "CMDBUFF":"${Math.floor(Math.random() * 20)}",
        "ACTIVE":"PRI",
        "ECHO_MODE":"${(i % 2 === 0 ? 'DISABLED' : 'ENABLED')}",
        "AGC1":"0x76",
        "AGC2":"0x00",
        "AGC3":"0x00",
        "AGC4":"0x00",
        "RWS":"${(i % 2 === 0 ? 'Off' : 'On')}",
        "IDLE_MODE":"STones",
        "CMD_RATE":"${Math.floor(Math.random() * 1000)}",
        "CMD_ENABLED":"${(i % 2 === 0 ? 'No' : 'Yes')}",
        "CMD_MODE":"${Math.floor(Math.random() * 5)}",
        "AZ_DELTA":"${number}",
        "EL_DELTA":"${number2}",
        "RANGE_DELTA":"${Math.random() * 12000}",
        "PCMSIM_ENABLED":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "SV_CMDS_SENT":"${Math.floor(Math.random() * 20)}",
        "SV_CMDS_RCVD":"${Math.floor(Math.random() * 20)}",
        "AGC_VALUE":"${Math.abs(70) - 160}"
        }
        `;
                // TODO: (statusMessage) add VEHICLE = 5-digit numeric string
                //TODO: sab slave angle buffer
                //random increment aggregate, 2 thirds chance plus one, one third chance minus one, up to 29
                //at 29 reverse it 1 third chance to go up, two thirds to go down. Reverse upon reaching zero. etc.

                let taskedArr = ['"RUNNING"', '"FAILED"', '"IDLE"']; //NO CLOSING NO SETUP
                updateMessage = `
    {
        "CMD_SRC":"${(i % 2 === 0 ? 'KS252' : 'TERNARY')}",
        "CMD_DST":"CCS",
        "CMD_ECHO":"${(i % 2 === 0 ? 'DISABLE' : 'ENABLE')}",
        "CMD_TERM":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "PRIOPEN":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "PRICONNECT":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "SECOPEN":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "SECCONNECT":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "SYSTIME": "${time}",
        "CLIENTS":${clientArr[i % 4]},
        "PRI_ADCCP_MSG_SENT":"${Math.abs(n += Math.floor(Math.random() * 6))}",
        "PRI_ADCCP_MSG_RCVD":"${Math.abs(o += Math.floor(Math.random() * 9))}",
        "SEC_ADCCP_MSG_SENT":"${Math.abs(p += Math.floor(Math.random() * 6))}",
        "SEC_ADCCP_MSG_RCVD":"${Math.abs(q += Math.floor(Math.random() * 9))}",
        "PRI_CONNECT_FAILURE":"${(i % 3 === 0 ? 'FALSE' : 'TRUE')}",
        "SEC_CONNECT_FAILURE":"${(i % 2 === 0 ? 'FALSE' : 'TRUE')}",
        "TASKSTATUS":${taskedArr[
                    // i % 3
                    taskindex
                    ]}
        }`;
                // TODO: (updateMessage) add CMD_SRC_STATUS = READY | FAILED | (empty string)

                // redisClient.lpush("APP.status", statusMessage, function (err, result) {
                // });
                // redisClient.ltrim("APP.status", 0, 99, function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });

                // redisClient.publish("APP.status", statusMessage);
                // redisClient.publish("APP.update", updateMessage);

                redisClient.publish("APP.update", `{PropName:${++messageNumber}}`);
                redisClient.publish("APP.status", `{PropName:${messageNumber/2}}`);
//
//                 redisClient.lpush("APP.update", updateMessage, function (err, result) {
//                 });
//                 redisClient.ltrim("APP.update", 0, 99, function (err, result) {
//                     // console.log("Error: " + err);
//                     // console.log("Result: " + result);
//                 });
// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                let logEntries = `14:01:41.937 V158007: Log File Opened C:/Program Files (x86)/BTI/AceSuite/AceCCT/Logs/COTS-CCT2_101017_140141.log
    14:01:42.777 V158004: ARTS Mode Selected
    14:01:42.785 V158014: POA information: POA Az=9.998, POA El=2.997, POA Range= 3443.920, POA Time=131521165800000000, POA SysTime=10/10/2017 13:43:00
    14:01:42.882 V156071: CCSOutputChB (len=2) >> 83,1F
    14:01:42.886 V156070: CCSOutputChA (len=2) >> 83,1F
    14:01:43.428 V156071: CCSOutputChB (len=2) >> 83,1F
    14:01:43.433 V156070: CCSOutputChA (len=2) >> 83,1F
    14:01:43.773 V156070: CCSOutputChA (len=214) >> 83,03,82,05,01,47,00,E4,00,3F,00,E4,00,3A,00,E4,00,35,00,E4,00,2F,00,E4,00,2A,00,E4,00,25,00,E4,00,20,00,E4,00,1A,00,E4,00,15,00,E4,00,10,01,48,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,49,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,50,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,51,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10
    14:01:44.930 V156070: CCSOutputChA (len=2) >> 83,1F
    14:01:44.935 V156071: CCSOutputChB (len=2) >> 83,1F
    14:01:45.773 V156070: CCSOutputChA (len=214) >> 83,03,82,05,01,52,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,53,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,54,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,55,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,56,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10
    14:01:47.771 V156070: CCSOutputChA (len=214) >> 83,03,82,05,01,57,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,58,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,01,59,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,00,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,01,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10
    14:01:49.772 V156070: CCSOutputChA (len=214) >> 83,03,82,05,02,02,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,03,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,04,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,05,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,06,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10
    14:01:51.773 V156070: CCSOutputChA (len=214) >> 83,03,82,05,02,07,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,08,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,09,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,11,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10
    14:01:53.774 V156070: CCSOutputChA (len=214) >> 83,03,82,05,02,12,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,13,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,14,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,15,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,02,16,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10,00,E4,00,10`.split('\n');

                // Write these log entries to the filepath that we send through the config message.

                //             redisClient.lpush("APP.log",
                //     // logEntries[
                //     logLine++
                //     // %logEntries.length]
                //     , function (err, result) {
                //         //console.log("Error: " + err);
                //         // console.log(logLine);
                //     });
                //             redisClient.ltrim("APP.log", 0, 1000, function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                ++i;
                k = Math.floor(Math.random() * 80);
                i = Math.floor(Math.random() * 160);
                j = Math.floor(Math.random() * 240);
                l = Math.floor(Math.random() * 300);
                m = Math.floor(Math.random() * 360);

                //EXAMPLES & SAMPLES


                // redisClient.lpush("ini", 'This represents an ini file', function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                // //Keeps only 1 at a time
                // redisClient.ltrim("ini", 0, 1, function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                //

                //
                //
                // //Communication back to the cct
                // redisClient.hset("returnChannel", "log", "null", function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                //
                // redisClient.hset("returnChannel", "updateIni", "null", function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                //
                // redisClient.hset("returnChannel", "updateConfig", "null", function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });


                // //KEYS
                // redisClient.hset("keys", "key1", "thisiskey1", function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                // redisClient.hset("keys", "key2", "thisiskey2", function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                // redisClient.hset("keys", "key3", "thisiskey3", function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });
                // redisClient.hgetall("keys", function (err, result) {
                //     //console.log("Error: " + err);
                //     console.dir(result);
                // });
                setTimeout(redisFlood, 1000) //should be 125
            }


            let configurationArr = [
                '{"primary":true, "secondary":true}',
                '{"primary":false, "secondary":true}',
                '{"primary":true, "secondary":false}'
            ];

            function redisConfigFlood() {
                configMessage = `
        {
        "IDLEMODE":"STONES",
            "CMDRATE":"1000",
            "CMDMSG_SIZE":"90",
            "OPS_LINK":"PRIMARY",
            "SLAVING":"ENABLED",
            "COMMANDING":"ENABLED",
            "CMD_IDLE_THRESHOLD":"100",
            "CMD_MODE":"1",
            "CMD_TERM_ENABLE":"FALSE",
            "RAWADCCP":"OFF",
            "RWS_ENABLE":"TRUE",
            "OPERMODE":"ARTS ALM SOC",
            "SITE":"COOK-A",
            "LATITUDE":"34.823",
            "LONGITUDE":"120.502"
        }
        `;
                f++;
                g++;


                // redisClient.set("config", `{"NAME":0,"CONFIGURED":${configurationArr[g % 3]}}`, function (err, result) {
                //     //console.log("Error: " + err);
                //     //console.log("Result: " + result);
                // });

                setTimeout(function () {
                    redisConfigFlood();
                }, 5000)
            }

            redisFlood();
            redisConfigFlood();
        }
        catch (exception) {
            reject(exception);
        }
    });
};

let redisMessagePromise = redisMessage();

redisMessagePromise.catch(function () {
    setTimeout(function () {
        console.log('connection attempt to redis');
        redisMessagePromise = redisMessage();
    }, 1000)
});