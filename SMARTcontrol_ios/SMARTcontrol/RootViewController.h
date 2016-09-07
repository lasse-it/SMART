//
//  RootViewController.h
//  SMARTcontrol
//
//  Created by Lasse Hyldahl Jensen on 20/09/15.
//  Copyright © 2015 Lasse Hyldahl Jensen. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RootViewController : UIViewController <UIPageViewControllerDelegate>

@property (strong, nonatomic) UIPageViewController *pageViewController;

@end

