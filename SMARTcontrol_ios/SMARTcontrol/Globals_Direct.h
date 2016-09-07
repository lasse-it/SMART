//
//  Globals_Direct.h
//  SMARTcontrol
//
//  Created by Lasse Hyldahl Jensen on 18/09/15.
//  Copyright © 2015 Lasse Hyldahl Jensen. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Globals_Direct : NSObject {
    NSMutableDictionary *_lights;
    NSMutableArray *_servers;
}

+ (Globals_Direct *)sharedInstance;

@property(strong, nonatomic, readwrite) NSMutableDictionary *lights;
@property(strong, nonatomic, readwrite) NSMutableArray *servers;
@end