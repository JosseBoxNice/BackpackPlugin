# BackpackPlugin

A lightweight Minecraft plugin that adds personal backpacks for players, saving items across server restarts.

## Features

- Multiple backpacks per player  
- Configurable size, titles and colors
- Persistent storage in YAML files  
- Simple commands and permissions  

## Installation

1. Place `BackpackPlugin.jar` into your server’s `plugins/` folder.  
2. Restart or reload the server.  
3. Configure `config.yml` to adjust default backpack size, max backpacks, and titles.

## Commands

- `/backpack [number]` — Opens your backpack #number (default 1)  
- `/backpacktitle <number> <title>` - Changes your backpacks for example /backpacktitle 1 &aMy backpack (is possible without colors)

## Permissions

  backpack.use:
    description: Allows the player to use backpacks
    default: false
  backpack.rename:
    description: Allows the player to rename their backpacks
    default: false
  backpack.color:
    description: Allows the player to use color codes (&) in backpack titles
    default: false
  backpack.max.1:
    description: Allows 1 backpack
    default: false
  backpack.max.2:
    description: Allows 2 backpacks
    default: false
  backpack.max.3:
    description: Allows 3 backpacks
    default: false
  backpack.max.5:
    description: Allows 5 backpacks
    default: false
  backpack.max.10:
    description: Allows 10 backpacks
    default: false
  backpack.size.9:
    description: 9 slot backpacks
    default: false
  backpack.size.18:
    description: 18 slot backpacks
    default: false
  backpack.size.27:
    description: 27 slot backpacks
    default: false
  backpack.size.36:
    description: 36 slot backpacks
    default: false
  backpack.size.45:
    description: 45 slot backpacks
    default: false
  backpack.size.54:
    description: 54 slot backpacks
    default: false


## Configuration

`config.yml`

backpack:
  default-size: 27
  max-backpacks: 3
  title: "&6Your Backpack %number%"
