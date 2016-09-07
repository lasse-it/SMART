//
//  ModelController.h
//  SMARTcontrol
//
//  Created by Lasse Hyldahl Jensen on 20/09/15.
//  Copyright © 2015 Lasse Hyldahl Jensen. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DataViewController;

@interface ModelController : NSObject <UIPageViewControllerDataSource>

- (DataViewController *)viewControllerAtIndex:(NSUInteger)index storyboard:(UIStoryboard *)storyboard;
- (NSUInteger)indexOfViewController:(DataViewController *)viewController;

@end

