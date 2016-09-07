//
//  Globals_Direct.m
//  SMARTcontrol
//  Example of Direct communication with the SMARTserver
//  Created by Lasse Hyldahl Jensen on 18/09/15.
//  Copyright © 2015 Lasse Hyldahl Jensen. All rights reserved.
//
#import "Globals_Direct.h"

@implementation Globals_Direct

@synthesize lights = _lights;

+ (Globals_Direct *)sharedInstance {
    static dispatch_once_t onceToken;
    static Globals_Direct *instance = nil;
    dispatch_once(&onceToken, ^{
        instance = [[Globals_Direct alloc] init];
    });
    return instance;
}

- (id)init {
    self = [super init];
    if (self) {
        //server config
        _servers = [[NSMutableArray alloc] init];
        for (int f = 0; f < 2; f++) {
            NSMutableDictionary *server = [[NSMutableDictionary alloc] init];
            [server setValue:@(0) forKey:(@"gateway")];
            [server setValue:@(0) forKey:(@"ssl")];
            [server setValue:(@"66putang") forKey:(@"token")];
            NSMutableArray *ports = [[NSMutableArray alloc] init];
            if (f == 0) { //10.0.1.106
                [server setValue:(@"10.0.1.106") forKey:(@"ip")];
                [server setValue:(@"lasse's room") forKey:(@"name")];
                for (int a = 0; a < 5; a++) {
                    NSMutableDictionary *port = [[NSMutableDictionary alloc] init];
                    switch (a) {
                        case 0:
                            [port setValue:@(17) forKey:(@"port")];
                            [port setValue:(@"Center") forKey:@"name"];
                            break;
                        case 1:
                            [port setValue:@(22) forKey:(@"port")];
                            [port setValue:(@"PC") forKey:@"name"];
                            break;
                        case 2:
                            [port setValue:@(23) forKey:(@"port")];
                            [port setValue:(@"Speakers") forKey:@"name"];
                            break;
                        case 3:
                            [port setValue:@(24) forKey:(@"port")];
                            [port setValue:(@"USB") forKey:@"name"];
                            break;
                        case 4:
                            [port setValue:@(27) forKey:(@"port")];
                            [port setValue:(@"Bed") forKey:@"name"];
                            break;
                    }
                    [ports insertObject:port atIndex:a];
                }
            } else if (f == 1) { //10.0.1.107
                [server setValue:(@"10.0.1.107") forKey:(@"ip")];
                [server setValue:(@"outside") forKey:(@"name")];
                for (int a = 0; a < 2; a++) {
                    NSMutableDictionary *port = [[NSMutableDictionary alloc] init];
                    switch (a) {
                        case 0:
                            [port setValue:@(23) forKey:(@"port")];
                            [port setValue:(@"Field") forKey:@"name"];
                            break;
                        case 1:
                            [port setValue:@(24) forKey:(@"port")];
                            [port setValue:(@"Courtyard") forKey:@"name"];
                            break;
                    }
                    [ports insertObject:port atIndex:a];
                }
            }
            [server setValue:ports forKey:(@"ports")];
            [_servers insertObject:server atIndex:f];
        }
        NSMutableArray *lightcat = [[NSMutableArray alloc] init];
        _lights = [[NSMutableDictionary alloc] init];
        for (int i = 0; i < 2; i++) {
            NSMutableArray *btn = [[NSMutableArray alloc] init];
            for (int a = 0; a < 3; a++) {
                NSMutableDictionary *btnsub = [[NSMutableDictionary alloc] init];
                    switch (a) {
                        case 0: //btn1
                            switch (i) {
                                case 0:
                                    [btnsub setValue:(@"ON") forKey:(@"title")];
                                    [btnsub setValue:(@"high-0-1") forKey:(@"cmd")];
                                    break;
                                case 1:
                                    [btnsub setValue:(@"ON") forKey:(@"title")];
                                    [btnsub setValue:(@"high-0-1-4") forKey:(@"cmd")];
                                    break;
                            }
                            break;
                        case 1: //btn2
                            switch (i) {
                                case 0:
                                    [btnsub setValue:(@"BLINK") forKey:(@"title")];
                                    [btnsub setValue:(@"blink-0-1") forKey:(@"cmd")];
                                    break;
                                case 1:
                                    [btnsub setValue:(@"BLINK") forKey:(@"title")];
                                    [btnsub setValue:(@"blink-0-1-4") forKey:(@"cmd")];
                                    break;
                            }
                            break;
                        
                        case 2: //btn3
                            switch (i) {
                                case 0:
                                    [btnsub setValue:(@"OFF") forKey:(@"title")];
                                    [btnsub setValue:(@"low-0-1") forKey:(@"cmd")];
                                    break;
                                case 1:
                                    [btnsub setValue:(@"OFF") forKey:(@"title")];
                                    [btnsub setValue:(@"low-0-1-4") forKey:(@"cmd")];
                                    break;
                            }
                            break;
                    }
                    if (i == 0) {
                        [btnsub setValue:@(1) forKey:(@"server")];
                    } else {
                        [btnsub setValue:@(0) forKey:(@"server")];
                    }
                    [btn insertObject:btnsub atIndex:a];
                }
                NSMutableDictionary *lightcatsub = [[NSMutableDictionary alloc] init];
                switch (i) {
                    case 0:
                        [lightcatsub setValue:(@"OUTDOOR") forKey:(@"title")];
                        break;
                    case 1:
                        [lightcatsub setValue:(@"LASSE") forKey:(@"title")];
                        break;
                }
            [lightcatsub setValue:(btn) forKey:(@"btn")];
            [lightcat insertObject:(lightcatsub) atIndex:(i)];
        }
        [_lights setValue:lightcat forKey:(@"lightcat")];
    }
    return self;
}
@end