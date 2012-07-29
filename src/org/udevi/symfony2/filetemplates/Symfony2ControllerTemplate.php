<?php

namespace ${namespaceDir};

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;

/**
 * Description of ${name}
 *
 * @author ${user}
 */
class ${name} extends Controller
{
    public function indexAction()
    {
        return new Response('Hello world!');
    }
}