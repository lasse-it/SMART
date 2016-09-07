using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using Windows.UI.Notifications;
using Windows.Data.Xml.Dom;
using System.Net;
using System.Threading.Tasks;
using System.Text;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=234238

namespace SMARTcontrol {
    public sealed partial class MainPage : Page {
        public Object item = "SMARTcontrol";
        public MainPage() {
            this.InitializeComponent();
            Controls.Visibility = Visibility.Collapsed;
            back.Visibility = Visibility.Visible;
            SMARTcontrol();
        }
        private void back_click(object sender, RoutedEventArgs e) {
            Menu.Items.Clear();
            Controls.Visibility = Visibility.Collapsed;
            if (item.Equals("LIGHT") || item.Equals("ALARM") || item.Equals("MUSIC") || item.Equals("SETTINGS"))
            {
                SMARTcontrol();
            } else if (item.Equals("LControls"))
            {
                LIGHT();
            }
        }

        private void Menu_click(object sender, ItemClickEventArgs e)
        {
            item = e.ClickedItem;
            back.Visibility = Visibility.Visible;
            Menu.Items.Clear();
            if (item.Equals("LIGHT")) {
                LIGHT();
            } else if (item.Equals("Lasse's Room") || item.Equals("Outside"))
            {
                LControls();
            } else if (item.Equals("ALARM"))
            {
                ALARM();
            } else if (item.Equals("MUSIC"))
            {
                MUSIC();
            } else if (item.Equals("SETTINGS"))
            {
                SETTINGS();
            }
        }
        public void SMARTcontrol ()
        {   
            item = "SMARTcontrol";
            MenuTitle.Text = item + "";
            back.Visibility = Visibility.Collapsed;
            Menu.Items.Add("LIGHT");
            Menu.Items.Add("ALARM");
            Menu.Items.Add("MUSIC");
            Menu.Items.Add("SETTINGS");
        }
        public void LIGHT ()
        {
            item = "LIGHT";
            MenuTitle.Text = item + "";
            Menu.Items.Add("Lasse's Room");
            Menu.Items.Add("Outside");
        }
        public void LControls()
        {
            MenuTitle.Text = item + "";
            item = "LControls";
            Controls.Visibility = Visibility.Visible;
        }
        public void ALARM ()
        {
            MenuTitle.Text = item + "";
        }
        public void MUSIC ()
        {
            MenuTitle.Text = item + "";
        }
        public void SETTINGS ()
        {
            MenuTitle.Text = item + "";
        }
        public async void cmd (String ip, String cmd)
        {
            // server to POST to
            string url = ip + "/controlv2.php?cmd=" + cmd;

            // HTTP web request
            var httpWebRequest = (HttpWebRequest)WebRequest.Create(url);
            httpWebRequest.ContentType = "text/plain; charset=utf-8";
            httpWebRequest.Method = "POST";

            // Write the request Asynchronously 
            using (var stream = await Task.Factory.FromAsync<Stream>(httpWebRequest.BeginGetRequestStream,
                                                                     httpWebRequest.EndGetRequestStream, null))
            {
                //create some json string
                string json = "";

                // convert json to byte array
                byte[] jsonAsBytes = Encoding.UTF8.GetBytes(json);

                // Write the bytes to the stream
                await stream.WriteAsync(jsonAsBytes, 0, jsonAsBytes.Length);
            }
        }

        private void btn_1btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-17-22-27");
        }
        private void btn_1btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-17-22-27");
        }
        private void btn_1btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-17-22-27");
        }
        private void btn_2btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-27");
        }
        private void btn_2btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-27");
        }
        private void btn_2btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-27");
        }
        private void btn_3btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-17");
        }
        private void btn_3btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-17");
        }
        private void btn_3btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-17");
        }
        private void btn_4btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-22");
        }
        private void btn_4btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-22");
        }
        private void btn_4btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-22");
        }
        private void btn_5btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "effects-stripe-0.2-17-22-27");
        }
        private void btn_5btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-23");
        }
        private void btn_5btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-23");
        }
        private void btn_6btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-17-22-27");
        }
        private void btn_6btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-17-22-27");
        }
        private void btn_6btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-17-22-27");
        }
        private void btn_7btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-17-22-27");
        }
        private void btn_7btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-17-22-27");
        }
        private void btn_7btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-17-22-27");
        }
        private void btn_8btn0(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "high-17-22-27");
        }
        private void btn_8btn1(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "blink-17-22-27");
        }
        private void btn_8btn2(object sender, TappedRoutedEventArgs e)
        {
            cmd("10.0.1.106", "low-17-22-27");
        }
    }
}
