//
//  RSSMasterViewController.m
//  MobiusRSS
//
//  Created by Alexey Ushakov on 2/28/14.
//  Copyright (c) 2014 jetbrains. All rights reserved.
//

#import "RSSMasterViewController.h"
#import "RSSTitlesController.h"
#import "Chrome.h"

@interface RSSMasterViewController () {
    NSMutableArray *_rssURLS;
}
@end

@implementation RSSMasterViewController {
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = NSLocalizedString(@"Mobius RSS Reader", @"Master");
        _rssURLS = [[NSMutableArray alloc] init];
    }
    return self;
}
							
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self addFeed:@"http://images.apple.com/main/rss/hotnews/hotnews.rss"];
    [self addFeed:@"http://www.jetbrains.com/rss.xml"];
    [self addFeed:@"http://news.yandex.ru/hardware.rss"];

    self.navigationItem.leftBarButtonItem = self.editButtonItem;
    UIBarButtonItem *addButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd
                                                                               target:self
                                                                               action:@selector(insertNewObject:)];

    self.navigationItem.rightBarButtonItem = addButton;

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)insertNewObject:(id)sender
{
    [Chrome alertViewTitle:@"RSS Feed" message:@"Input rss feed" delegate:self];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)text {
    NSString* url = [[alertView textFieldAtIndex:0] text];
    [_rssURLS insertObject:url atIndex:0];

    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:0];
    [self.tableView insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationAutomatic];
}

#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;

}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _rssURLS.count;

}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }

    cell.textLabel.font = [UIFont systemFontOfSize:14];
    cell.textLabel.text = _rssURLS[(NSUInteger) indexPath.row];
    return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        [self removeFeed:(NSUInteger) indexPath.row];
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }

}

- (void)removeFeed:(NSUInteger)i {
    [_rssURLS removeObjectAtIndex:i];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (!self.rssTitlesController) {
        self.rssTitlesController = [[RSSTitlesController alloc] initWithNibName:@"RSSTitlesController" bundle:nil];
    }
    self.rssTitlesController.detailItem = _rssURLS[(NSUInteger) indexPath.row];
    [self.navigationController pushViewController:self.rssTitlesController animated:YES];
    [self.rssTitlesController reload];
}

@end