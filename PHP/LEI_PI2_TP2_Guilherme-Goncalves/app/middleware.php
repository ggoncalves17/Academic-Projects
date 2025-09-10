<?php

declare(strict_types=1);

use App\Application\Middleware\SessionMiddleware;
use App\Application\Middleware\AuthMiddleware;
use App\Application\Middleware\CORSMiddleware;

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

use Slim\App;

return function (App $app) {
    // Auth Middleware
    $app->add(AuthMiddleware::class);
};
