using System;
using System.Collections.Generic;
using System.IO;

namespace csharp
{
    class SyncSongs
    {
        
        private const string DestinationDirectoryPath = "ToPhone";
        private const string DropboxEnvironment = "DROPBOX";
        private const string KillFileEnvironment = "SONGS_KILL_FILE";
        private const string Mp3Filter = "*.mp3";
        private const string SourceDirectoryPath = "Bike-MS-2019";

        private static List<string> KillFile = new List<string>();

        public static string DestinationDirectory
        {
            get => $"{Environment.GetEnvironmentVariable(DropboxEnvironment)}\\{DestinationDirectoryPath}";
        }
        
        public static string KillFilePath 
        { 
            get => $"{Environment.GetEnvironmentVariable(KillFileEnvironment)}";
        }

        public static string SourceDirectory
        {
            get => $"{Environment.GetEnvironmentVariable(DropboxEnvironment)}\\{SourceDirectoryPath}";
        }


        static void Main(string[] args)
        {
            ReadKillFile();

            var destinationDirectory = DestinationDirectory;
            var sourceDirectory = SourceDirectory;
           
            Console.WriteLine($"Coping files form {sourceDirectory} to {destinationDirectory}.");
            Console.WriteLine("Press \"enter\" to continue.");
            Console.ReadLine();

            DirectoryInfo di = new DirectoryInfo(sourceDirectory);

            foreach (var fi in di.GetFiles(Mp3Filter))
            {
                if (KillFile.Contains(fi.Name))
                {
                    continue;
                }

                if (File.Exists($"{destinationDirectory}\\{fi.Name}"))
                {
                    continue;
                }
                
                Console.WriteLine($"Copying {fi.FullName} to {destinationDirectory}\\{fi.Name}.");
                fi.CopyTo($"{destinationDirectory}\\{fi.Name}", false);
            }
        }

        private static void CheckEnvironement()
        {
            if (String.IsNullOrEmpty(Environment.GetEnvironmentVariable(DropboxEnvironment)) ||
                String.IsNullOrEmpty(Environment.GetEnvironmentVariable(KillFileEnvironment)))
                {
                    Console.WriteLine();
                    Console.WriteLine("Unable to detect required Environment variables: {DropboxEnvironment} or {KillFileEnvironment}.");
                    Environment.Exit(1);
                }
        }
        
        private static void ReadKillFile()
        {
            foreach(var line in File.ReadAllLines(KillFilePath))
            {
               KillFile.Add(line);
            }
        }
    }
}
