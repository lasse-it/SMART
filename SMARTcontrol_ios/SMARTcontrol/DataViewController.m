//
//  DataViewController.m
//  SMARTcontrol
//
//  Created by Lasse Hyldahl Jensen on 20/09/15.
//  Copyright © 2015 Lasse Hyldahl Jensen. All rights reserved.
//

#import "DataViewController.h"
#import "Globals.h"

@interface DataViewController ()

@end

@implementation DataViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [super viewWillAppear:animated];
    Globals *gbs = [Globals sharedInstance];
    NSMutableArray *lights = [gbs.lights objectForKey:(@"lightcat")];
    NSString *title = [self.dataObject description];
    NSUInteger index = getIndex(title);
    NSString *btn1title = [[[[lights objectAtIndex:index] objectForKey:@"btn"] objectAtIndex:0] objectForKey:(@"title")];
    NSString *btn2title = [[[[lights objectAtIndex:index] objectForKey:@"btn"] objectAtIndex:1] objectForKey:(@"title")];
    NSString *btn3title = [[[[lights objectAtIndex:index] objectForKey:@"btn"] objectAtIndex:2] objectForKey:(@"title")];
    [_btn1 setTitle:btn1title forState:UIControlStateNormal];
    [_btn2 setTitle:btn2title forState:UIControlStateNormal];
    [_btn3 setTitle:btn3title forState:UIControlStateNormal];
    _btn1.titleLabel.numberOfLines = 1;
    _btn1.titleLabel.adjustsFontSizeToFitWidth = YES;
    _btn1.titleLabel.lineBreakMode = NSLineBreakByClipping;
    _btn2.titleLabel.numberOfLines = 1;
    _btn2.titleLabel.adjustsFontSizeToFitWidth = YES;
    _btn2.titleLabel.lineBreakMode = NSLineBreakByClipping;
    _btn3.titleLabel.numberOfLines = 1;
    _btn3.titleLabel.adjustsFontSizeToFitWidth = YES;
    _btn3.titleLabel.lineBreakMode = NSLineBreakByClipping;
    self.dataLabel.text = title;
}

- (IBAction)btn1:(id)sender {
    NSString *title = [self.dataObject description];
    SendCmd(1, title);
}

- (IBAction)btn2:(id)sender {
    NSString *title = [self.dataObject description];
    SendCmd(2, title);
}

- (IBAction)btn3:(id)sender {
    NSString *title = [self.dataObject description];
    SendCmd(3, title);
}

NSUInteger getIndex(NSString *title) {
    Globals *gbs = [Globals sharedInstance];
    NSMutableArray *lights = [gbs.lights objectForKey:(@"lightcat")];
    NSUInteger slides = [lights count];
    NSUInteger index = 0;
    for (NSUInteger i=0; i<slides; i++) {
        NSString *jsontitle = [[lights objectAtIndex:i] objectForKey:(@"title")];
        if (title == jsontitle) {
            index = i;
        }
    }
    return index;
}

void SendCmd(int btn, NSString *title) {
    Globals *gbs = [Globals sharedInstance];
    NSMutableArray *lights = [gbs.lights objectForKey:(@"lightcat")];
    NSMutableArray *servers = gbs.servers;
    NSUInteger index = getIndex(title);
    NSMutableString *command = [[NSMutableString alloc] init];
    NSUInteger serverid = [[[[[lights objectAtIndex:index] objectForKey:(@"btn")] objectAtIndex:(btn -1)] objectForKey:(@"server")] integerValue];
    NSMutableDictionary *server = [servers objectAtIndex:serverid];
    NSUInteger gateway = [[server objectForKey:(@"gateway")] integerValue];
    NSString *ip = [server objectForKey:(@"ip")];
    NSUInteger ssl = [[server objectForKey:(@"ssl")] integerValue];
    NSString *cmd = [[[[lights objectAtIndex:index] objectForKey:(@"btn")] objectAtIndex:(btn -1)] objectForKey:(@"cmd")];
    NSUInteger redirect = [[[[[lights objectAtIndex:index] objectForKey:(@"btn")] objectAtIndex:(btn -1)] objectForKey:(@"redirect")] integerValue];
    NSArray *taskarr = [cmd componentsSeparatedByString:(@"_")];
    NSMutableString *cmdfix = [[NSMutableString alloc] init];
    NSCharacterSet *letters = [NSCharacterSet characterSetWithCharactersInString:@"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"];
    letters = [letters invertedSet];
    if (ssl == 1) {
        [command appendString:(@"https://")];
    } else {
        [command appendString:(@"http://")];
    }
    [command appendString:(ip)];
    [command appendString:(@"/controlv2.php?cmd=")];
    if (gateway == 1) {
        for (int a = 0; a < taskarr.count; a++) {
            NSArray *cmdarr = [[taskarr objectAtIndex:a] componentsSeparatedByString:(@"-")];
            for (int i = 0; i < cmdarr.count; i++) {
                NSRange r = [[cmdarr objectAtIndex:i] rangeOfCharacterFromSet:letters];
                if (r.location == NSNotFound || [[cmdarr objectAtIndex:i-1] isEqual: @"sleep"] || [[cmdarr objectAtIndex:i-1]  isEqual: @"volume"] || [[cmdarr objectAtIndex:i-1]  isEqual: @"play"] || [[cmdarr objectAtIndex:i] containsString:@"."]) {
                    [cmdfix appendString:[cmdarr objectAtIndex:i]];
                } else {
                    NSUInteger portid = [[cmdarr objectAtIndex:i] integerValue];
                    NSString *port = [[[[[[server objectForKey:(@"redirect")] objectAtIndex:redirect] objectForKey:(@"ports")] objectAtIndex:portid] objectForKey:(@"port")] stringValue];
                    [cmdfix appendString:(port)];
                }
                [cmdfix appendString:(@"-")];
            }
            [cmdfix deleteCharactersInRange:NSMakeRange([cmdfix length]-1, 1)];
            [cmdfix appendString:(@"_")];
        }
        [cmdfix deleteCharactersInRange:NSMakeRange([cmdfix length]-1, 1)];
        [command appendString:(cmdfix)];
        [command appendString:(@"&token=")];
        [command appendString:([[[server objectForKey:(@"redirect")] objectAtIndex:redirect] objectForKey:(@"token")])];
        [command appendString:(@"&ip=")];
        [command appendString:([[[server objectForKey:(@"redirect")] objectAtIndex:redirect] objectForKey:(@"ip")])];
    } else {
        for (int a = 0; a < taskarr.count; a++) {
            NSArray *cmdarr = [[taskarr objectAtIndex:a] componentsSeparatedByString:(@"-")];
            for (int i = 0; i < cmdarr.count; i++) {
                NSRange r = [[cmdarr objectAtIndex:i] rangeOfCharacterFromSet:letters];
                if (r.location == NSNotFound || [[cmdarr objectAtIndex:i-1] isEqual: @"sleep"] || [[cmdarr objectAtIndex:i-1]  isEqual: @"volume"] || [[cmdarr objectAtIndex:i-1]  isEqual: @"play"] || [[cmdarr objectAtIndex:i] containsString:@"."]) {
                    [cmdfix appendString:[cmdarr objectAtIndex:i]];
                } else {
                    NSUInteger portid = [[cmdarr objectAtIndex:i] integerValue];
                    NSString *port = [[[[server objectForKey:(@"ports")] objectAtIndex:portid] objectForKey:(@"port")] stringValue];
                    [cmdfix appendString:(port)];
                }
                [cmdfix appendString:(@"-")];
            }
            [cmdfix deleteCharactersInRange:NSMakeRange([cmdfix length]-1, 1)];
            [cmdfix appendString:(@"_")];
        }
        [cmdfix deleteCharactersInRange:NSMakeRange([cmdfix length]-1, 1)];
        [command appendString:(cmdfix)];
        [command appendString:(@"&token=")];
        [command appendString:([server objectForKey:(@"token")])];
    }
    NSLog(@"%@, %@", [NSString stringWithFormat:command, nil], nil);
    NSURLRequest *urlRequest = [NSURLRequest requestWithURL:[NSURL URLWithString:command]];
    NSURLResponse *response = nil;
    NSError *error = nil;
    NSData *data = [NSURLConnection sendSynchronousRequest:urlRequest returningResponse:&response error:&error];
}

@end
